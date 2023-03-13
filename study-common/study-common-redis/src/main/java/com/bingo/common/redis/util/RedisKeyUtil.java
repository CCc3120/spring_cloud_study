package com.bingo.common.redis.util;

import com.bingo.study.common.core.utils.SpringUtil;
import com.bingo.study.common.core.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * redis key 操作工具类
 *
 * @author bingo
 * @date 2022-04-27 16:18
 */
@Slf4j
public class RedisKeyUtil {

    /**
     * 分割符
     */
    public static final String DECOLLATOR = StringUtil.SEPARATOR_COLON;

    /**
     * 应用前缀
     */
    public static String APP_PREFIX;

    static {
        APP_PREFIX = SpringUtil.getValue("spring.application.name", String.class, "");
        if (StringUtil.isNull(APP_PREFIX)) {
            log.warn("application name is null");
        }
    }

    /**
     * 永不过期的缓存名
     */
    public static final String CACHE_NAME_FOREVER = "forever";

    /**
     * 获取缓存key名称
     *
     * @param prefix key前缀
     * @param name   缓存key名称
     * @return
     */
    public static String getCacheKey(String prefix, String name, boolean isForever) {
        return getCacheKey(prefix, name, isForever, false);
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
            redisKey.append(APP_PREFIX).append(DECOLLATOR);
        }
        if (isForever) {
            redisKey.append(CACHE_NAME_FOREVER).append(DECOLLATOR);
        }
        redisKey.append(prefix).append(DECOLLATOR).append(name);
        return redisKey.toString();
    }
}
