package com.bingo.study.common.component.dict.service.impl;

import com.bingo.common.redis.service.RedisService;
import com.bingo.common.redis.util.RedisKeyUtil;
import com.bingo.study.common.component.dict.service.IDictCacheService;
import com.bingo.study.common.core.dict.IDictCategoryModel;
import com.bingo.study.common.core.dict.IDictDataModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 采用redis 双Map缓存字典数据，分别缓存 IDictCategoryModel 和 IDictDataModel
 *
 * @Author h-bingo
 * @Date 2023-08-10 16:19
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractDictRedisCacheService<C extends IDictCategoryModel, D extends IDictDataModel>
        extends AbstractDictTranslateService<D> implements IDictCacheService<C, D> {

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
    private String getDictKey(String type) {
        return RedisKeyUtil.getCacheKey(String.format(type, this.getDictKeyPrefix()), false, true);
    }

    /***
     * 不同字典，必须保证唯一
     * @Param []
     * @Return java.lang.String
     * @Date 2023-08-14 15:03
     */
    protected abstract String getDictKeyPrefix();

    @Override
    public void setDict(Map<C, List<D>> dictMap) {
        String categoryKey = this.getDictKey(DICT_CATEGORY_KEY);
        String dataKey = this.getDictKey(DICT_DATA_KEY);
        for (Map.Entry<C, List<D>> entry : dictMap.entrySet()) {
            this.setDict(entry.getKey(), entry.getValue(), categoryKey, dataKey);
        }
    }

    @Override
    public void setDict(C type, List<D> dictList) {
        this.setDict(type, dictList, this.getDictKey(DICT_CATEGORY_KEY), this.getDictKey(DICT_DATA_KEY));
    }

    private void setDict(C type, List<D> dictList, String categoryKey, String dataKey) {
        redisService.opsForHash().put(categoryKey, type.getFdType(), dictList);
        redisService.opsForHash().put(dataKey, type.getFdType(), dictList);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<D> getDict(String type) {
        String dataKey = this.getDictKey(DICT_DATA_KEY);
        return (List<D>) redisService.opsForHash().get(dataKey, type);
    }

    @Override
    public D getDict(String code, String type) {
        List<D> dictCache = this.getDict(type);
        for (D dictDataModel : dictCache) {
            if (dictDataModel.getFdCode().equals(code)) {
                return dictDataModel;
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<C, List<D>> getDict() {
        String categoryKey = this.getDictKey(DICT_CATEGORY_KEY);
        String dataKey = this.getDictKey(DICT_DATA_KEY);

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
    public void removeDict() {
        String categoryKey = this.getDictKey(DICT_CATEGORY_KEY);
        String dataKey = this.getDictKey(DICT_DATA_KEY);
        redisService.deleteObject(categoryKey);
        redisService.deleteObject(dataKey);
    }

    @Override
    public void removeDict(String type) {
        String categoryKey = this.getDictKey(DICT_CATEGORY_KEY);
        String dataKey = this.getDictKey(DICT_DATA_KEY);
        this.removeDict(type, categoryKey, dataKey);
    }

    private void removeDict(String type, String categoryKey, String dataKey) {
        redisService.opsForHash().delete(categoryKey, type);
        redisService.opsForHash().delete(dataKey, type);
    }

    @Override
    public void refreshDict(C type, List<D> dictList) {
        this.refreshDict(type, dictList, this.getDictKey(DICT_CATEGORY_KEY), this.getDictKey(DICT_DATA_KEY));
    }

    public void refreshDict(C type, List<D> dictList, String categoryKey, String dataKey) {
        this.removeDict(type.getFdType(), categoryKey, dataKey);
        this.setDict(type, dictList, categoryKey, dataKey);
    }

    @Override
    public void refreshDict(Map<C, List<D>> dictMap) {
        String categoryKey = this.getDictKey(DICT_CATEGORY_KEY);
        String dataKey = this.getDictKey(DICT_DATA_KEY);
        for (Map.Entry<C, List<D>> entry : dictMap.entrySet()) {
            this.refreshDict(entry.getKey(), entry.getValue(), categoryKey, dataKey);
        }
    }
}
