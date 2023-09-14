package com.bingo.study.common.component.limiter.aspect;

import cn.hutool.core.date.SystemClock;
import cn.hutool.core.util.RandomUtil;
import com.bingo.common.redis.service.RedisService;
import com.bingo.common.redis.util.RedisKeyUtil;
import com.bingo.study.common.component.limiter.LimitRealize;
import com.bingo.study.common.component.limiter.LimitType;
import com.bingo.study.common.component.limiter.LimitUserFactory;
import com.bingo.study.common.component.limiter.annotation.RateLimiter;
import com.bingo.study.common.component.limiter.exception.RateLimiterException;
import com.bingo.study.common.core.utils.AspectUtil;
import com.bingo.study.common.core.utils.IPUtil;
import com.bingo.study.common.core.utils.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.redis.core.script.RedisScript;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Set;

/**
 * @Author h-bingo
 * @Date 2023-04-26 09:32
 * @Version 1.0
 */
@Slf4j
@Aspect
@ConditionalOnMissingBean(RateLimiterAspect.class)
public class RateLimiterAspect implements InitializingBean {

    private static final String LIMITER_KEY = "rateLimiter:";

    @Autowired
    private RedisService redisService;

    @Resource(name = "rateLimiterScript")
    private RedisScript<Long> redisScript;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired(required = false)
    private LimitUserFactory limitUserFactory;

    @Pointcut("@annotation(com.bingo.study.common.component.limiter.annotation.RateLimiter)")
    public void pointcut() {
    }

    @Before("pointcut()&&@annotation(limiter)")
    public void doBefore(JoinPoint point, RateLimiter limiter) {
        if (LimitRealize.TOKEN_BUCKET == limiter.limitRealize()) { // 令牌桶
            this.tokenBucket(point, limiter);
        } else if (LimitRealize.FIXED_WINDOW == limiter.limitRealize()) { // 固定窗口
            this.fixedWindow(point, limiter);
        } else if (LimitRealize.SLIDING_WINDOW == limiter.limitRealize()) { // 滑动窗口
            this.slidingWindow(point, limiter);
        }
    }

    private void tokenBucket(JoinPoint point, RateLimiter limiter) {
        String redisLimiterKey = this.getRedisLimiterKey(point, limiter);
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(redisLimiterKey);

        if (!rateLimiter.isExists()) {
            // 设置令牌桶速率
            rateLimiter.trySetRate(RateType.OVERALL, limiter.count(), limiter.time(), RateIntervalUnit.SECONDS);
        }

        RateLimiterConfig rateLimiterConfig = rateLimiter.getConfig();

        if (rateLimiterConfig.getRate() != limiter.count()
                || rateLimiterConfig.getRateInterval() != RateIntervalUnit.SECONDS.toMillis(limiter.time())) {

            // 如果和之前配置不一样，则删除，重新设置
            rateLimiter.delete();
            rateLimiter.trySetRate(RateType.OVERALL, limiter.count(), limiter.time(), RateIntervalUnit.SECONDS);
        }

        if (!rateLimiter.tryAcquire(1)) {
            log.info("方法已限流: {}", redisLimiterKey);
            throw new RateLimiterException("访问过于频繁，请稍候再试");
        }
    }

    private void fixedWindow(JoinPoint point, RateLimiter limiter) {
        String redisLimiterKey = this.getRedisLimiterKey(point, limiter);
        Long number = redisService.redisTemplate().execute(redisScript, Collections.singletonList(redisLimiterKey),
                limiter.count(), limiter.time());
        if (number != null && number > limiter.count()) {
            log.info("方法已限流: {}", redisLimiterKey);
            throw new RateLimiterException("访问过于频繁，请稍候再试");
        }
    }

    private void slidingWindow(JoinPoint point, RateLimiter limiter) {
        String redisLimiterKey = this.getRedisLimiterKey(point, limiter);
        long currentTime = SystemClock.now();
        Set<Object> range = redisService.opsForZSet().rangeByScore(redisLimiterKey,
                currentTime - limiter.time() * 1000, currentTime);
        int currentCount = range == null ? 0 : range.size();
        if (currentCount >= limiter.count()) { // size 从0开始
            log.info("方法已限流: {}", redisLimiterKey);
            throw new RateLimiterException("访问过于频繁，请稍候再试");
        }
        // 移除过期的
        redisService.opsForZSet().removeRangeByScore(redisLimiterKey, 0, currentTime - limiter.time() * 1000);
        // 添加当前时间值
        redisService.opsForZSet().add(redisLimiterKey, currentTime + RandomUtil.randomInt(), currentTime);
    }

    /**
     * 获取限流key
     * <p>
     * 组成：applicationName + {@link RateLimiterAspect#LIMITER_KEY} + 方法名 + 限流方式 + （ip）（用户id）
     *
     * @Param [point, limiter]
     * @Return java.lang.String
     * @Date 2023-04-26 11:46
     */
    private String getRedisLimiterKey(JoinPoint point, RateLimiter limiter) {
        StringBuilder builder = new StringBuilder(LIMITER_KEY);
        builder.append(AspectUtil.getMethodIntactName(point));
        builder.append(":").append(limiter.limitRealize().name());

        if (limiter.limitType() == LimitType.IP) {
            builder.append(":").append(IPUtil.getIpAddr(ServletUtil.getRequest()));
        } else if (limiter.limitType() == LimitType.USER) {
            if (limitUserFactory == null) {
                log.warn("未实现获取用户id方法: {}", LimitUserFactory.class.getTypeName());
                throw new RateLimiterException("未实现获取用户id方法");
            }
            builder.append(":").append(limitUserFactory.getUserId());
        }

        return RedisKeyUtil.getCacheKey(builder.toString(), false, true);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("接口限流功能已开启，请在需要限流接口添加: @RateLimiter");
    }
}
