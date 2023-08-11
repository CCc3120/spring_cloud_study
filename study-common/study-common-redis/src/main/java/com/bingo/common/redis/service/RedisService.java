package com.bingo.common.redis.service;

import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedisService
 * @Description redis相关操作
 * @Author h-bingo
 * @Date 2023-01-04 10:00
 * @Version 1.0
 */
@SuppressWarnings({"unused"})
@Component
public class RedisService {

    private final RedisTemplate<Object, Object> redisTemplate;

    public RedisService(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 判断 key 是否存在
     *
     * @param key 键
     * @return 如果存在 key 则返回 true，否则返回 false
     */
    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 获取过期时间
     *
     * @param key 键
     * @return 返回key的过期时间
     */
    public Long expire(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(String key, T value) {
        opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key     缓存的键值
     * @param value   缓存的值
     * @param timeout 时间
     */
    public <T> void setCacheObject(String key, T value, long timeout) {
        setCacheObject(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setCacheObject(String key, T value, long timeout, TimeUnit timeUnit) {
        opsForValue().set(key, value, timeout, timeUnit);
    }


    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(String key, long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return true=设置成功；false=设置失败
     */
    @SuppressWarnings({"all"})
    public boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    @SuppressWarnings({"unchecked"})
    public <T> T getCacheObject(String key) {
        return (T) opsForValue().get(key);
    }

    /**
     * 删除单个对象
     *
     * @param key
     */
    @SuppressWarnings({"all"})
    public boolean deleteObject(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return
     */
    @SuppressWarnings({"all"})
    public long deleteObject(Collection<Object> collection) {
        return redisTemplate.delete(collection);
    }

    public RedisTemplate<Object, Object> redisTemplate() {
        return redisTemplate;
    }

    public ValueOperations<Object, Object> opsForValue() {
        return redisTemplate.opsForValue();
    }

    public HashOperations<Object, Object, Object> opsForHash() {
        return redisTemplate.opsForHash();
    }

    public ListOperations<Object, Object> opsForList() {
        return redisTemplate.opsForList();
    }

    public SetOperations<Object, Object> opsForSet() {
        return redisTemplate.opsForSet();
    }

    public ZSetOperations<Object, Object> opsForZSet() {
        return redisTemplate.opsForZSet();
    }
}
