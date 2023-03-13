package com.bingo.study.common.component.cache;

import com.bingo.common.redis.service.RedisService;
import com.bingo.study.common.component.cache.handle.MemoryCache;
import com.bingo.study.common.component.cache.handle.MemoryRedisCache;
import com.bingo.study.common.component.cache.handle.RedisCache;
import com.bingo.study.common.core.utils.SpringUtil;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.Type;

public abstract class CacheBase implements InitializingBean {

    protected CacheManager cacheManager;

    public abstract String cacheType();

    public abstract Type type();

    @Override
    public void afterPropertiesSet() throws Exception {
        cacheManager = CacheManager.getDefaultCacheManager(cacheType());
        cacheManager.addCache(MemoryCache.CACHE_TYPE, new MemoryCache(type()));
        cacheManager.addCache(RedisCache.CACHE_TYPE, new RedisCache(type(), SpringUtil.getBean(RedisService.class)));
        cacheManager.addCache(MemoryRedisCache.CACHE_TYPE, new MemoryRedisCache(type(),
                SpringUtil.getBean(RedisService.class)));
    }
}
