package com.bingo.study.common.component.dict.service.impl;

import com.bingo.study.common.component.dict.service.IDictCacheService;
import com.bingo.study.common.component.dict.service.IDictCategoryDbService;
import com.bingo.study.common.component.dict.service.IDictDataDbService;
import com.bingo.study.common.component.dict.service.IDictService;
import com.bingo.study.common.core.dict.IDictCategoryModel;
import com.bingo.study.common.core.dict.IDictDataModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 该类是包装一层操作缓存的数据，组合大于继承
 * <p>
 * 例：
 * 继承改类，并且注入Spring中，即可使用 {@link IDictService} 接口中的方法
 * 1、字典类型数据 service 需实现 {@link IDictCategoryDbService}
 * 2、字典数据 service 需实现 {@link IDictDataDbService}
 *
 * @Author h-bingo
 * @Date 2023-08-11 10:22
 * @Version 1.0
 */
public abstract class AbstractDictService<C extends IDictCategoryModel, D extends IDictDataModel>
        extends AbstractDictTranslateService<D> implements IDictService<C, D> {

    @Autowired
    private IDictCacheService<C, D> dictCacheService;

    @Autowired
    private IDictCategoryDbService<C> dictCategoryDbService;

    @Autowired
    private IDictDataDbService<D> dictDataDbService;

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

    @Override
    public List<D> getDictFromDb(String type) {
        return dictDataDbService.getDictDataFromDb(type);
    }

    @Override
    public D getDictFromDb(String code, String type) {
        return dictDataDbService.getDictDataFromDb(code, type);
    }

    @Override
    public Map<C, List<D>> getDictFromDb() {
        Map<C, List<D>> rtnMap = new HashMap<>();
        List<C> cList = dictCategoryDbService.getDictCategoryFromDb();
        for (C c : cList) {
            rtnMap.put(c, this.getDictFromDb(c.getFdType()));
        }
        return rtnMap;
    }
}
