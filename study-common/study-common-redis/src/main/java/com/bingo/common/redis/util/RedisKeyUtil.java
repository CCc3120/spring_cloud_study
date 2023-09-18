package com.bingo.common.redis.util;

import com.bingo.study.common.core.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 * redis key 操作工具类
 *
 * @author bingo
 * @date 2022-04-27 16:18
 */
@Slf4j
@SuppressWarnings("unused")
public class RedisKeyUtil {

    /**
     * 分割符
     */
    public static final String SEPARATOR_COLON = StringUtil.SEPARATOR_COLON;

    /**
     * 应用前缀
     */
    public static String APP_PREFIX;

    @Value("${spring.application.name}")
    public void setAppPrefix(String appPrefix) {
        RedisKeyUtil.APP_PREFIX = StringUtil.join(appPrefix.split("-"), ":");
    }

    /**
     * 永不过期的缓存名
     */
    public static final String CACHE_NAME_FOREVER = "forever";

    /**
     * 获取缓存key名称
     *
     * @param prefix    key前缀
     * @param name      缓存key名称
     * @param isForever 是否永久
     * @return
     */
    public static String getCacheKey(String prefix, String name, boolean isForever) {
        return getCacheKey(prefix, name, isForever, false);
    }

    /**
     * 获取缓存key名称
     *
     * @param name         缓存key名称
     * @param hasAppPrefix 是否带应用前缀
     * @return
     */
    public static String getCacheKey(String name, boolean hasAppPrefix) {
        return getCacheKey(null, name, false, hasAppPrefix);
    }

    /**
     * 获取缓存key名称
     *
     * @param name         缓存key名称
     * @param isForever    是否永久
     * @param hasAppPrefix 是否带应用前缀
     * @return
     */
    public static String getCacheKey(String name, boolean isForever, boolean hasAppPrefix) {
        return getCacheKey(null, name, isForever, hasAppPrefix);
    }

    /**
     * 获取缓存key名称
     *
     * @param prefix       key前缀
     * @param name         缓存key名称
     * @param hasAppPrefix 是否带应用前缀
     * @return
     */
    public static String getCacheKey(String prefix, String name, boolean isForever, boolean hasAppPrefix) {
        StringBuilder redisKey = new StringBuilder();
        if (hasAppPrefix) {
            redisKey.append(APP_PREFIX).append(SEPARATOR_COLON);
        }
        if (isForever) {
            redisKey.append(CACHE_NAME_FOREVER).append(SEPARATOR_COLON);
        }

        if (!StringUtil.isNull(prefix)) {
            redisKey.append(prefix).append(SEPARATOR_COLON);
        }
        redisKey.append(name);
        return redisKey.toString();
    }
}

