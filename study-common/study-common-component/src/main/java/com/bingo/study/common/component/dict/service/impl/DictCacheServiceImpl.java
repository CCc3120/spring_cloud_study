package com.bingo.study.common.component.dict.service.impl;

import com.bingo.common.redis.service.RedisService;
import com.bingo.common.redis.util.RedisKeyUtil;
import com.bingo.study.common.component.dict.service.IDictCacheService;
import com.bingo.study.common.core.dict.IDictCategoryModel;
import com.bingo.study.common.core.dict.IDictDataModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.function.Consumer;

/**
 * 采用redis 双Map缓存字典数据，分别缓存 IDictCategoryModel 和 IDictDataModel
 *
 * @Author h-bingo
 * @Date 2023-08-10 16:19
 * @Version 1.0
 */
@Slf4j
public abstract class DictCacheServiceImpl<C extends IDictCategoryModel, D extends IDictDataModel>
        implements IDictCacheService<C, D> {

    private static final String DICT_CATEGORY_KEY = "dict:%s:dictCategory";

    private static final String DICT_DATA_KEY = "dict:%s:dictData";

    @Autowired
    private RedisService redisService;

    /***
     * key组成
     * applicationName + DICT_TYPE_KEY/DICT_DATA_KEY
     *
     * @Param [type, keyMark]
     * @Return java.lang.String
     * @Date 2023-08-11 09:50
     */
    private String getDictCacheKey(String type, String keyMark) {
        return RedisKeyUtil.getCacheKey(String.format(type, keyMark), false, true);
    }

    protected abstract String getDictCategoryKey();

    protected abstract String getDictDataKey();

    @Override
    public void setDictCache(Map<C, List<D>> dictMap) {
        String categoryKey = getDictCacheKey(DICT_CATEGORY_KEY, getDictCategoryKey());
        String dataKey = getDictCacheKey(DICT_DATA_KEY, getDictDataKey());
        for (Map.Entry<C, List<D>> entry : dictMap.entrySet()) {
            setDictCache(entry.getKey(), entry.getValue(), categoryKey, dataKey);
        }
    }

    @Override
    public void setDictCache(C type, List<D> dictList) {
        setDictCache(type, dictList, getDictCacheKey(DICT_CATEGORY_KEY, getDictCategoryKey()),
                getDictCacheKey(DICT_DATA_KEY, getDictDataKey()));
    }

    private void setDictCache(C type, List<D> dictList, String categoryKey, String dataKey) {
        redisService.opsForHash().put(categoryKey, type.getType(), dictList);
        redisService.opsForHash().put(dataKey, type.getType(), dictList);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<D> getDictCache(String type) {
        String dataKey = getDictCacheKey(DICT_DATA_KEY, getDictDataKey());
        return (List<D>) redisService.opsForHash().get(dataKey, type);
    }

    @Override
    public D getDictCache(String code, String type) {
        List<D> dictCache = getDictCache(type);
        for (D dictDataModel : dictCache) {
            if (dictDataModel.getCode().equals(code)) {
                return dictDataModel;
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<C, List<D>> getDictCache() {
        String categoryKey = getDictCacheKey(DICT_CATEGORY_KEY, getDictCategoryKey());
        String dataKey = getDictCacheKey(DICT_DATA_KEY, getDictDataKey());

        Set<Object> keys = redisService.opsForHash().keys(categoryKey);

        Map<C, List<D>> dictMap = new LinkedHashMap<>();
        for (Object key : keys) {
            C c = (C) redisService.opsForHash().get(categoryKey, key);
            List<D> dictData = (List<D>) redisService.opsForHash().get(dataKey, key);
            dictMap.put(c, dictData);
        }
        return dictMap;
    }

    @Override
    public void removeDictCache() {
        String categoryKey = getDictCacheKey(DICT_CATEGORY_KEY, getDictCategoryKey());
        String dataKey = getDictCacheKey(DICT_DATA_KEY, getDictDataKey());
        redisService.deleteObject(categoryKey);
        redisService.deleteObject(dataKey);
    }

    @Override
    public void removeDictCache(String type) {
        String categoryKey = getDictCacheKey(DICT_CATEGORY_KEY, getDictCategoryKey());
        String dataKey = getDictCacheKey(DICT_DATA_KEY, getDictDataKey());
        removeDictCache(type, categoryKey, dataKey);
    }

    private void removeDictCache(String type, String categoryKey, String dataKey) {
        redisService.opsForHash().delete(categoryKey, type);
        redisService.opsForHash().delete(dataKey, type);
    }

    @Override
    public void refreshDictCache(C type, List<D> dictList) {
        refreshDictCache(type, dictList, getDictCacheKey(DICT_CATEGORY_KEY, getDictCategoryKey()),
                getDictCacheKey(DICT_DATA_KEY, getDictDataKey()));
    }

    public void refreshDictCache(C type, List<D> dictList, String categoryKey, String dataKey) {
        removeDictCache(type.getType(), categoryKey, dataKey);

        redisService.opsForHash().put(categoryKey, type.getType(), type);
        redisService.opsForHash().put(dataKey, type.getType(), dictList);
    }

    @Override
    public void refreshDictCache(Map<C, List<D>> dictMap) {
        String categoryKey = getDictCacheKey(DICT_CATEGORY_KEY, getDictCategoryKey());
        String dataKey = getDictCacheKey(DICT_DATA_KEY, getDictDataKey());
        for (Map.Entry<C, List<D>> entry : dictMap.entrySet()) {
            refreshDictCache(entry.getKey(), entry.getValue(), categoryKey, dataKey);
        }
    }

    @Override
    public Optional<D> getDictOpt(String code, String type) {
        return Optional.ofNullable(getDictCache(code, type));
    }

    @Override
    public void dictTran(String code, String type, Consumer<D> consumer) {
        getDictOpt(code, type).ifPresent(consumer);
    }
}
