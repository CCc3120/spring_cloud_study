package com.bingo.study.common.component.cache.handle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bingo.common.redis.service.RedisService;
import com.bingo.study.common.component.cache.Cache;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * tip：内存缓存在使用集群的时候会存在缓存不同步的问题，请注意使用场景
 */
public class MemoryRedisCache implements Cache {

    public static final String CACHE_TYPE = "MemoryRedisCache";

    private String namespace = "MemoryRedisCache";

    private static Map<String, Object> memoryCache = new ConcurrentHashMap<>();
    private Type type;

    private RedisService redisService;

    public MemoryRedisCache(Type type, RedisService redisService) {
        this.type = type;
        this.redisService = redisService;
    }

    @Override
    public <T> T addObject(String key, T data) {
        if ("java.lang.String".equals(type.getTypeName())) {
            JSONObject object = new JSONObject();
            object.put(namespace, data);
            redisService.setCacheObject(key, object);
        } else {
            redisService.setCacheObject(key, JSON.parse(JSON.toJSONString(data)));
        }
        memoryCache.put(key, data);
        return data;
    }

    @Override
    public <T> T getObject(String key) {
        T data = (T) memoryCache.get(key);
        if (data == null) {
            JSON json = redisService.getCacheObject(key);
            if (json == null) {
                return null;
            }
            if ("java.lang.String".equals(type.getTypeName())) {
                return (T) ((JSONObject) json).getObject(namespace, type);
            } else if (JSON.class.getTypeName().equals(type.getTypeName())) {
                return (T) json;
            } else if (json instanceof JSONObject) {
                return JSON.parseObject(json.toJSONString(), type);
            } else if (json instanceof JSONArray) {
                return (T) JSON.parseArray(json.toJSONString(), new Type[]{type});
            }
        }
        return data;
    }

    @Override
    public void removeObject(String key) {
        memoryCache.remove(key);
        redisService.deleteObject(key);
    }
}
