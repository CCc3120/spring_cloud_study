package com.bingo.study.common.component.limiter.annotation;

import com.bingo.common.redis.config.EnableRedis;
import com.bingo.study.common.component.limiter.aspect.RateLimiterAspect;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author h-bingo
 * @Date 2023-04-26 10:39
 * @Version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({RateLimiterAspect.class})
@EnableRedis
public @interface EnableRateLimiter {
}
