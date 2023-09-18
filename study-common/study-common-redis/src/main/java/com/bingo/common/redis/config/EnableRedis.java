package com.bingo.common.redis.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author h-bingo
 * @Date 2023-09-18 10:18
 * @Version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({RedisAutoConfig.class})
public @interface EnableRedis {
}
