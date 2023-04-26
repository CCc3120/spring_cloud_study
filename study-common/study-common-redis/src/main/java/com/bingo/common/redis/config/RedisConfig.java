package com.bingo.common.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @author bingo
 * @date 2022-04-11 16:01
 */
@EnableCaching // 开启缓存注解
@Configuration
// org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration优先该类中的RedisTemplate的bean，
// bean中就不会在此初始化了@ConditionalOnMissingBean(name = "redisTemplate")
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class RedisConfig extends CachingConfigurerSupport {

    /**
     * 自定义RedisTemplate
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        // 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);

        serializer.setObjectMapper(mapper);

        // 设置key-value key 和value序列化模式
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);

        // 设置hash key 和value序列化模式
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);

        // 此方法bean实例化的时候会调用 为什么还要执行一次
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    /**
     * 自定义缓存管理器
     *
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisTemplate<Object, Object> redisTemplate) {
        // 采用redis缓存配置 自定义缓存数据序列化方式和有效期限
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                // 使用和redisTemplate一致的对key进行数据类型转换
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getStringSerializer()))
                // 使用和redisTemplate一致的对value的数据类型进行转换
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer()))
                // 设置缓存有效期为1天
                .entryTtl(Duration.ofDays(1))
                // 对空数据不操作
                .disableCachingNullValues();
        return RedisCacheManager
                .builder(redisTemplate.getRequiredConnectionFactory())
                .cacheDefaults(config)
                // 配置同步修改或删除 put/evict
                .transactionAware()
                .build();
    }

    @Bean(name = "rateLimiterScript")
    public DefaultRedisScript<Long> rateLimiterScript() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(limitScriptText());
        redisScript.setResultType(Long.class);
        return redisScript;
    }

    /**
     * 限流脚本
     */
    private String limitScriptText() {
        return "local key = KEYS[1]\n" +
                "local count = tonumber(ARGV[1])\n" +
                "local time = tonumber(ARGV[2])\n" +
                "local current = redis.call('get', key);\n" +
                "if current and tonumber(current) > count then\n" +
                "    return tonumber(current);\n" +
                "end\n" +
                "current = redis.call('incr', key)\n" +
                "if tonumber(current) == 1 then\n" +
                "    redis.call('expire', key, time)\n" +
                "end\n" +
                "return tonumber(current);";
    }

    private String slidingWindowText() {
        return "local key = KEYS[1]\n" +
                "local currentTime = tonumber(ARGV[1])\n" +
                "local ttl = tonumber(ARGV[2])\n" +
                "local windowTime = tonumber(ARGV[3]) --\n" +
                "local limitCount = tonumber(ARGV[4])\n" +
                "local value = tonumber(ARGV[5])\n" +
                "redis.call('zremrangebyscore', key, 0, currentTime - windowTime)\n" +
                "local currentNum = tonumber(redis.call('zcard', key))\n" +
                "local next = currentNum + 1\n" +
                "if next > limitCount then\n" +
                "return 0;\n" +
                "else\n" +
                "redis.call(\"zadd\", key, currentTime, value)\n" +
                "redis.call(\"expire\", key, ttl)\n" +
                "return next\n" +
                "end";
    }
}
