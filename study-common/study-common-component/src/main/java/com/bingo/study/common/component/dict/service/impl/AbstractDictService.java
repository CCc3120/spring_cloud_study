package com.bingo.study.common.component.dict.service.impl;

import com.bingo.study.common.component.dict.service.IDictCacheService;
import com.bingo.study.common.component.dict.service.IDictService;
import com.bingo.study.common.core.dict.IDictCategoryModel;
import com.bingo.study.common.core.dict.IDictDataModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 该类是包装一层操作缓存的数据，组合大于继承
 *
 * @Author h-bingo
 * @Date 2023-08-11 10:22
 * @Version 1.0
 */
public abstract class AbstractDictService<C extends IDictCategoryModel, D extends IDictDataModel>
        extends AbstractDictTranslateService<D> implements IDictService<C, D> {

    @Autowired
    private IDictCacheService<C, D> dictCacheService;

    @Override
    public List<D> getDict(String type) {
        return dictCacheService.getDict(type);
    }

    @Override
    public D getDict(String code, String type) {
        return dictCacheService.getDict(code, type);
    }

    @Override
    public Map<C, List<D>> getDict() {
        return dictCacheService.getDict();
    }

    @Override
    public void removeDict(String type) {
        dictCacheService.removeDict(type);
    }

    @Override
    public void refreshDict(C type, List<D> dictList) {
        dictCacheService.refreshDict(type, dictList);
    }

    @Override
    public void refreshDict(Map<C, List<D>> dictMap) {
        dictCacheService.refreshDict(dictMap);
    }
}
