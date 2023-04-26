package com.bingo.study.common.component.limiter.aspect;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.SystemClock;
import cn.hutool.core.util.RandomUtil;
import com.bingo.common.redis.service.RedisService;
import com.bingo.common.redis.util.RedisKeyUtil;
import com.bingo.study.common.component.limiter.LimitRealize;
import com.bingo.study.common.component.limiter.LimitType;
import com.bingo.study.common.component.limiter.annotation.RateLimiter;
import com.bingo.study.common.component.limiter.exception.RateLimiterException;
import com.bingo.study.common.core.utils.IPUtil;
import com.bingo.study.common.core.utils.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author h-bingo
 * @Date 2023-04-26 09:32
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
@ConditionalOnMissingBean(RateLimiterAspect.class)
public class RateLimiterAspect implements InitializingBean {

    private static final String LIMITER_KEY = "rateLimiter:";

    @Autowired
    private RedisService redisService;

    @Resource(name = "rateLimiterScript")
    private RedisScript<Long> redisScript;

    @Autowired
    private RedissonClient redissonClient;

    @Pointcut("@annotation(com.bingo.study.common.component.limiter.annotation.RateLimiter)")
    public void rateLimiter() {
    }

    @Before("rateLimiter()&&@annotation(limiter)")
    public void doBefore(JoinPoint point, RateLimiter limiter) {
        if (LimitRealize.TOKEN_BUCKET == limiter.limitRealize()) { // 令牌桶
            tokenBucket(point, limiter);
        } else if (LimitRealize.FIXED_WINDOW == limiter.limitRealize()) { // 固定窗口
            fixedWindow(point, limiter);
        } else if (LimitRealize.SLIDING_WINDOW == limiter.limitRealize()) { // 滑动窗口
            slidingWindow(point, limiter);
        }
    }

    private void tokenBucket(JoinPoint point, RateLimiter limiter) {
        String redisLimiterKey = getRedisLimiterKey(point, limiter);
        // Long expire = redisService.expire(redisLimiterKey);
        // if (expire == null || expire == -1) {
        //     redisService.deleteObject(redisLimiterKey);
        // }
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(redisLimiterKey);
        if (rateLimiter.getConfig() == null) {
            rateLimiter.trySetRate(RateType.OVERALL, limiter.count(), limiter.time(), RateIntervalUnit.SECONDS);
        }
        if (!rateLimiter.tryAcquire(1)) {
            throw new RateLimiterException("访问过于频繁，请稍候再试");
        }
        log.info("限制请求,拿到令牌桶,缓存key'{}'", redisLimiterKey);
    }

    private void fixedWindow(JoinPoint point, RateLimiter limiter) {
        String redisLimiterKey = getRedisLimiterKey(point, limiter);
        Long number = redisService.redisTemplate().execute(redisScript, Collections.singletonList(redisLimiterKey),
                limiter.count(), limiter.time());
        if (number == null || number > limiter.count()) {
            throw new RateLimiterException("访问过于频繁，请稍候再试");
        }
        log.info("限制请求'{}',当前请求'{}',缓存key'{}'", limiter.count(), number, redisLimiterKey);
    }

    private void slidingWindow(JoinPoint point, RateLimiter limiter) {
        String redisLimiterKey = getRedisLimiterKey(point, limiter);
        long currentTime = SystemClock.now();
        Set<Object> range = redisService.opsForZSet().rangeByScore(redisLimiterKey,
                currentTime - limiter.time() * 1000, currentTime);
        int currentCount = range == null ? 0 : range.size();
        if (currentCount >= limiter.count()) { // size 从0开始
            throw new RateLimiterException("访问过于频繁，请稍候再试");
        }
        // 移除过期的
        redisService.opsForZSet().removeRangeByScore(redisLimiterKey, 0, currentTime - limiter.time() * 1000);
        // 添加当前时间值
        redisService.opsForZSet().add(redisLimiterKey, currentTime + RandomUtil.randomInt(), currentTime);
        log.info("限制请求'{}',当前请求'{}',缓存key'{}'", limiter.count(), currentCount, redisLimiterKey);
    }

    /**
     * 获取限流key
     * <p>
     * 组成：applicationName + {@link RateLimiterAspect#LIMITER_KEY} + 方法名 + 限流方式 + （ip）
     *
     * @Param [point, limiter]
     * @Return java.lang.String
     * @Date 2023-04-26 11:46
     */
    private String getRedisLimiterKey(JoinPoint point, RateLimiter limiter) {
        StringBuilder builder = new StringBuilder(LIMITER_KEY);
        builder.append(getMethodIntactName(point));
        builder.append(":").append(limiter.limitRealize().name());

        if (limiter.limitType() == LimitType.IP) {
            builder.append(":").append(IPUtil.getIpAddr(ServletUtil.getRequest()));
        }

        return RedisKeyUtil.getCacheKey(builder.toString(), false, true);
    }

    /**
     * 获取完整的方法名
     * <p>
     * 例如本方法：com.bingo.study.common.component.limiter.aspect.RateLimiterAspect.getMethodIntactName(ProceedingJoinPoint)
     *
     * @Param [joinPoint]
     * @Return java.lang.String
     * @Date 2023-04-25 14:13
     */
    private String getMethodIntactName(JoinPoint point) {
        String className = point.getTarget().getClass().getTypeName();
        MethodSignature ms = (MethodSignature) point.getSignature();
        String methodName = ms.getName();
        Class<?>[] parameterTypes = ms.getParameterTypes();
        List<String> collect = Arrays.stream(parameterTypes).map(Class::getSimpleName).collect(Collectors.toList());

        StringBuilder res = new StringBuilder(className);
        res.append(".").append(methodName).append("(");
        if (!CollectionUtil.isEmpty(collect)) {
            res.append(StringUtils.join(collect, ","));
        }
        res.append(")");
        return res.toString();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("接口限流功能已开启，请在需要限流接口添加: @RateLimiter");
    }
}
