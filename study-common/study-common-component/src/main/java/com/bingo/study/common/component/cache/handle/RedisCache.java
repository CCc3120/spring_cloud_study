package com.bingo.study.common.component.cache.handle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bingo.common.redis.service.RedisService;
import com.bingo.study.common.component.cache.Cache;

import java.lang.reflect.Type;

public class RedisCache implements Cache {

    public static final String CACHE_TYPE = "RedisCache";

    private String namespace = "RedisCache";

    private Type type;

    private RedisService redisService;

    public RedisCache(Type type, RedisService redisService) {
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
        return data;
    }

    @Override
    public <T> T getObject(String key) {
        JSON json = redisService.getCacheObject(key);
        if (json == null) {
            return null;
        }
        if ("java.lang.String".equals(type.getTypeName())) {
            return (T) ((JSONObject) json).getString(namespace);
        } else if (JSON.class.getTypeName().equals(type.getTypeName())) {
            return (T) json;
        } else if (json instanceof JSONObject) {
            return JSON.parseObject(json.toJSONString(), type);
        } else if (json instanceof JSONArray) {
            return (T) JSON.parseArray(json.toJSONString(), new Type[]{type});
        }
        return null;
    }

    @Override
    public void removeObject(String key) {
        redisService.deleteObject(key);
    }
}
