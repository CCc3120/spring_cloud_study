package com.bingo.study.common.component.lock.aspect;

import cn.hutool.core.util.ArrayUtil;
import com.bingo.common.redis.util.RedisKeyUtil;
import com.bingo.study.common.component.lock.LockType;
import com.bingo.study.common.component.lock.RedisLockCallBack;
import com.bingo.study.common.component.lock.annotation.RedisLock;
import com.bingo.study.common.component.lock.exception.RedisLockException;
import com.bingo.study.common.core.utils.AspectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import java.util.Arrays;
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
    public void redisLock() {
    }

    @Around(value = "redisLock()&&@annotation(lock)")
    public Object doAround(ProceedingJoinPoint joinPoint, RedisLock lock) throws Throwable {
        Object[] args = joinPoint.getArgs();

        checkParam(joinPoint, args, lock);

        String lockKey = getLockKey(joinPoint, args, lock);

        return doLock(joinPoint, lock, lockKey, joinPoint::proceed);
    }

    private Object doLock(ProceedingJoinPoint joinPoint, RedisLock lock, String key, RedisLockCallBack callBack)
            throws Throwable {
        log.debug("RedisLock Key: {}", key);
        RLock rLock = redissonClient.getLock(key);
        if (lock.lockType() == LockType.MUTEX) {
            try {
                if (rLock.isLocked()) {
                    String methodName = AspectUtil.getMethodIntactName(joinPoint);
                    log.info("当前方法已被加锁[{}]", methodName);
                    throw new RedisLockException(String.format("RedisLock获取锁失败[%s]", methodName));
                }

                rLock.lock(lock.leaseTime(), TimeUnit.MILLISECONDS);
                // 执行方法
                return callBack.doWork();
            } finally {
                unLock(rLock);
            }
        } else if (lock.lockType() == LockType.AUTO_RENEWAL_MUTEX) {
            try {
                if (rLock.isLocked()) {
                    String methodName = AspectUtil.getMethodIntactName(joinPoint);
                    log.info("当前方法已被加锁[{}]", methodName);
                    throw new RedisLockException(String.format("RedisLock获取锁失败[%s]", methodName));
                }

                rLock.lock();
                // 执行方法
                return callBack.doWork();
            } finally {
                unLock(rLock);
            }
        } else if (lock.lockType() == LockType.SYNC) {
            try {
                boolean tryLock = rLock.tryLock(lock.waitTime(), lock.leaseTime(), TimeUnit.MILLISECONDS);
                if (tryLock) {
                    // 执行方法
                    return callBack.doWork();
                } else {
                    String methodName = AspectUtil.getMethodIntactName(joinPoint);
                    log.error("RedisLock获取锁失败[{}]", methodName);
                    throw new RedisLockException(String.format("RedisLock获取锁失败[%s]", methodName));
                }
            } finally {
                unLock(rLock);
            }
        } else if (lock.lockType() == LockType.AUTO_RENEWAL_SYNC) {
            try {
                boolean tryLock = rLock.tryLock(lock.waitTime(), TimeUnit.MILLISECONDS);
                if (tryLock) {
                    // 执行方法
                    return callBack.doWork();
                } else {
                    String methodName = AspectUtil.getMethodIntactName(joinPoint);
                    log.error("RedisLock获取锁失败[{}]", methodName);
                    throw new RedisLockException(String.format("RedisLock获取锁失败[%s]", methodName));
                }
            } finally {
                unLock(rLock);
            }
        }
        String methodName = AspectUtil.getMethodIntactName(joinPoint);
        log.warn("RedisLock锁类型异常[{}]", methodName);
        throw new RedisLockException(String.format("RedisLock锁类型异常[%s]", methodName));
    }

    private void unLock(RLock rLock) {
        if (rLock.isLocked() && rLock.isHeldByCurrentThread()) {
            rLock.unlock();
        }
    }

    /**
     * 若hasId为true，则第一个参数必须不为空
     *
     * @Param [joinPoint, args, lock]
     * @Return void
     * @Date 2023-04-25 11:02
     */
    private void checkParam(ProceedingJoinPoint joinPoint, Object[] args, RedisLock lock) {
        if (lock.hasId()) {
            if (ArrayUtil.isEmpty(args) || args[0] == null) {
                String methodName = AspectUtil.getMethodIntactName(joinPoint);
                log.error("加锁对象id参数缺失，method = {}，args = {}", methodName, Arrays.toString(args));
                throw new RedisLockException("加锁对象id参数缺失");
            }
        }
    }

    /**
     * 如果 {@link RedisLock#hasId()} 为false
     * key 组成 applicationName + {@link RedisLockAspect#REDIS_KEY_PREFIX} + {@link RedisLock#key()}
     * <p>
     * 如果 {@link RedisLock#hasId()} 为true
     * key 组成 applicationName + {@link RedisLockAspect#REDIS_KEY_PREFIX} + {@link RedisLock#key()} + 方法第一个参数
     * <p>
     * {@link RedisLock#key()} 为空则用方法名取代
     *
     * @Param [joinPoint, args, lock]
     * @Return java.lang.String
     * @Date 2023-04-25 10:54
     */
    private String getLockKey(ProceedingJoinPoint joinPoint, Object[] args, RedisLock lock) {
        StringBuilder key = new StringBuilder(REDIS_KEY_PREFIX);

        if (StringUtils.isBlank(lock.key())) {
            key.append(AspectUtil.getMethodIntactName(joinPoint));
        } else {
            key.append(lock.key());
        }

        if (lock.hasId()) {
            key.append(":").append(args[0]);
        }

        return RedisKeyUtil.getCacheKey(key.toString(), false, true);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Redis分布式锁功能已开启，请在加锁方法添加: @RedisLock");
    }
}
