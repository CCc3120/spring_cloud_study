package com.bingo.study.common.component.lock.aspect;

import cn.hutool.core.util.ArrayUtil;
import com.bingo.common.redis.util.RedisKeyUtil;
import com.bingo.study.common.component.lock.LockType;
import com.bingo.study.common.component.lock.RedisLockCallBack;
import com.bingo.study.common.component.lock.annotation.LockKey;
import com.bingo.study.common.component.lock.annotation.RedisLock;
import com.bingo.study.common.component.lock.exception.RedisLockException;
import com.bingo.study.common.core.utils.AspectUtil;
import com.bingo.study.common.core.utils.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * RedisLockAspect 实现
 *
 * @Author h-bingo
 * @Date 2023-04-24 17:37
 * @Version 1.0
 */
@Slf4j
@Aspect
@ConditionalOnMissingBean(RedisLockAspect.class)
public class RedisLockAspect implements InitializingBean {

    private static final String REDIS_KEY_PREFIX = "redisLock:";

    @Autowired
    private RedissonClient redissonClient;

    @Pointcut("@annotation(com.bingo.study.common.component.lock.annotation.RedisLock)")
    public void pointcut() {
    }

    @Around(value = "pointcut()&&@annotation(lock)")
    public Object doAround(ProceedingJoinPoint joinPoint, RedisLock lock) throws Throwable {
        String lockId = this.getLockId(joinPoint, lock);
        String lockKey = this.getLockKey(joinPoint, lockId, lock);

        if (lock.lockType() == LockType.MUTEX) {
            return this.doLock(joinPoint, 0, lock.leaseTime(), lockKey, joinPoint::proceed);
        } else if (lock.lockType() == LockType.AUTO_RENEWAL_MUTEX) {
            return this.doLock(joinPoint, 0, -1, lockKey, joinPoint::proceed);
        } else if (lock.lockType() == LockType.SYNC) {
            return this.doLock(joinPoint, lock.waitTime(), lock.leaseTime(), lockKey, joinPoint::proceed);
        } else if (lock.lockType() == LockType.AUTO_RENEWAL_SYNC) {
            return this.doLock(joinPoint, lock.waitTime(), -1, lockKey, joinPoint::proceed);
        }
        String methodName = AspectUtil.getMethodIntactName(joinPoint);
        log.warn("RedisLock锁类型异常[{}]", methodName);
        throw new RedisLockException(String.format("RedisLock锁类型异常[%s]", methodName));
    }

    private Object doLock(ProceedingJoinPoint joinPoint, long waitTime, long leaseTime, String lockKey,
            RedisLockCallBack callBack) throws Throwable {
        log.info("RedisLock Key: {}", lockKey);

        RLock rLock = redissonClient.getLock(lockKey);
        try {
            boolean tryLock = rLock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS);
            if (tryLock) {
                // 执行方法
                return callBack.doWork();
            } else {
                String methodName = AspectUtil.getMethodIntactName(joinPoint);
                log.info("RedisLock获取锁失败[{}]", methodName);
                throw new RedisLockException(String.format("RedisLock获取锁失败[%s]", methodName));
            }
        } finally {
            this.unLock(rLock);
        }
    }

    private void unLock(RLock rLock) {
        if (rLock.isLocked() && rLock.isHeldByCurrentThread()) {
            rLock.unlock();
        }
    }

    /**
     * 若singleton为true，必须要有一个唯一的参数作为加锁key的一部分，并且这个参数要用 {@link LockKey} 注解标识
     *
     * @Param [joinPoint, lock]
     * @Return void
     * @Date 2023-04-25 11:02
     */
    private String getLockId(ProceedingJoinPoint joinPoint, RedisLock lock) {
        if (!lock.singleton()) {
            return null;
        }

        Object[] args = joinPoint.getArgs();
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();
        Parameter[] parameters = method.getParameters();
        if (StringUtils.isNotBlank(lock.paramName())) {
            ExpressionParser parser = new SpelExpressionParser();
            StandardEvaluationContext context = new StandardEvaluationContext();
            LocalVariableTableParameterNameDiscoverer localVariableTable =
                    new LocalVariableTableParameterNameDiscoverer();
            String[] parameterNameArr = localVariableTable.getParameterNames(method);
            if (ArrayUtil.isNotEmpty(parameterNameArr)) {
                for (int i = 0; i < parameterNameArr.length; i++) {
                    context.setVariable(parameterNameArr[i], args[i]);
                }
                Expression expression = parser.parseExpression(lock.paramName());
                return expression.getValue(context, String.class);
            }
        } else {
            if (parameters != null && parameters.length > 0) {
                for (int i = 0; i < parameters.length; i++) {
                    LockKey annotation = parameters[i].getAnnotation(LockKey.class);
                    if (annotation != null) {
                        if (StringUtils.isBlank(annotation.alias())) {
                            return args[i].toString();
                        }
                        @SuppressWarnings("rawtypes")
                        HashMap hashMap =
                                JsonMapper.getInstance().fromJson(JsonMapper.getInstance().toJsonString(args[i]),
                                        HashMap.class);
                        return hashMap.get(annotation.alias()).toString();
                    }
                }
            }
        }

        String methodName = AspectUtil.getMethodIntactName(joinPoint);
        log.error("缺少 paramName 属性 或 @LockKey 标识的唯一参数，method = {}，args = {}", methodName, Arrays.toString(args));
        throw new RedisLockException("缺少 @LockKey 标识的唯一参数");
    }

    /**
     * 如果 {@link RedisLock#singleton()} 为false
     * key 组成 applicationName + {@link RedisLockAspect#REDIS_KEY_PREFIX} + {@link RedisLock#keyPrefix()}
     * <p>
     * 如果 {@link RedisLock#singleton()} 为true
     * key 组成 applicationName + {@link RedisLockAspect#REDIS_KEY_PREFIX} + {@link RedisLock#keyPrefix()} +
     * 方法 @LockKey 参数
     * <p>
     * {@link RedisLock#keyPrefix()} ()} 为空则用方法名取代
     *
     * @Param [joinPoint, lockId, lock]
     * @Return java.lang.String
     * @Date 2023-04-25 10:54
     */
    private String getLockKey(ProceedingJoinPoint joinPoint, String lockId, RedisLock lock) {
        StringBuilder key = new StringBuilder(REDIS_KEY_PREFIX);

        if (StringUtils.isBlank(lock.keyPrefix())) {
            key.append(AspectUtil.getMethodIntactName(joinPoint));
        } else {
            key.append(lock.keyPrefix());
        }

        if (lock.singleton()) {
            key.append(":").append(lockId);
        }

        return RedisKeyUtil.getCacheKey(key.toString(), false, true);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Redis分布式锁功能已开启，请在加锁方法添加: @RedisLock");
    }
}
