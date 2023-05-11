package com.bingo.study.common.component.cache.handle;


import com.bingo.study.common.component.cache.Cache;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * tip：内存缓存在使用集群的时候应该是会存在缓存不同步的问题，请注意使用场景
 */
public class MemoryCache implements Cache {

    public static final String CACHE_TYPE = "MemoryCache";

    private static final Map<String, Object> memoryCache = new ConcurrentHashMap<>();

    private Type type;

    public MemoryCache(Type type) {
        this.type = type;
    }

    @Override
    public <T> T addObject(String key, T data) {
        memoryCache.put(key, data);
        return data;
    }

    @Override
    public <T> T getObject(String key) {
        return (T) memoryCache.get(key);
    }

    @Override
    public void removeObject(String key) {
        memoryCache.remove(key);
    }
}
