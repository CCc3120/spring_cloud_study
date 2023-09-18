package com.bingo.study.common.component.cache;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class CacheManager {

    private final String cacheType;

    private static final Map<String, Cache> cacheMap = new ConcurrentHashMap<>();

    private CacheManager(String cacheType) {
        this.cacheType = cacheType;
    }

    public void addCache(String key, Cache cache) {
        cacheMap.put(key, cache);
    }

    public static CacheManager getDefaultCacheManager(String cacheType) {
        return new CacheManager(cacheType);
    }

    private Cache getCache() {
        return cacheMap.get(cacheType);
    }

    public <T> T getObject(String key) {
        return getCache().getObject(key);
    }

    public <T> T putObject(String key, T data) {
        return putObject(key, data, 0L);
    }

    public <T> T putObject(String key, T data, long timeout) {
        return putObject(key, data, timeout, TimeUnit.SECONDS);
    }

    public <T> T putObject(String key, T data, long timeout, TimeUnit unit) {
        if (timeout > 0) {
            Timer timer = new Timer();
            int hashCode = data.hashCode();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (getCache().getObject(key) != null && hashCode == getCache().getObject(key).hashCode()) {
                        getCache().removeObject(key);
                    }
                }
            }, unit.toMillis(timeout));
        }
        return getCache().addObject(key, data);
    }

    public void removeObject(String key) {
        getCache().removeObject(key);
    }
}
