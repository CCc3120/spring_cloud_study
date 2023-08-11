package com.bingo.study.common.component.dict.service.impl;

import com.bingo.study.common.component.dict.service.IDictCacheService;
import com.bingo.study.common.component.dict.service.IDictService;
import com.bingo.study.common.core.dict.IDictCategoryModel;
import com.bingo.study.common.core.dict.IDictDataModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @Author h-bingo
 * @Date 2023-08-11 10:22
 * @Version 1.0
 */
public abstract class DictServiceImpl<C extends IDictCategoryModel, D extends IDictDataModel>
        implements IDictService<C, D> {

    @Autowired
    private IDictCacheService<C, D> dictCacheService;

    @Override
    public List<D> getDict(String type) {
        return dictCacheService.getDictCache(type);
    }

    @Override
    public D getDict(String code, String type) {
        return dictCacheService.getDictCache(code, type);
    }

    @Override
    public Map<C, List<D>> getDict() {
        return dictCacheService.getDictCache();
    }

    @Override
    public void removeDict(String type) {
        dictCacheService.removeDictCache(type);
    }

    @Override
    public void refreshDict(C type, List<D> dictList) {
        dictCacheService.refreshDictCache(type, dictList);
    }

    @Override
    public void refreshDict(Map<C, List<D>> dictMap) {
        dictCacheService.refreshDictCache(dictMap);
    }

    @Override
    public Optional<D> getDictOpt(String code, String type) {
        return dictCacheService.getDictOpt(code, type);
    }

    @Override
    public void dictTran(String code, String type, Consumer<D> consumer) {
        dictCacheService.dictTran(code, type, consumer);
    }
}
