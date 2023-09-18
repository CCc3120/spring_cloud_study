package com.bingo.common.redis.config;

import com.bingo.common.redis.service.RedisService;
import com.bingo.common.redis.util.RedisKeyUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @Author h-bingo
 * @Date 2023-09-18 11:24
 * @Version 1.0
 */
@Import(RedisConfig.class)
@ConditionalOnMissingBean(RedisAutoConfig.class)
public class RedisAutoConfig {

    @Bean
    @ConditionalOnMissingBean(RedisKeyUtil.class)
    public RedisKeyUtil redisKeyUtil() {
        return new RedisKeyUtil();
    }

    @Bean
    @ConditionalOnMissingBean(RedisService.class)
    public RedisService redisService(RedisTemplate<Object, Object> redisTemplate) {
        return new RedisService(redisTemplate);
    }
}
