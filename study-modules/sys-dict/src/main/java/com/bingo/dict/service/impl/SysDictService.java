package com.bingo.dict.service.impl;

import com.bingo.dict.model.SysDictCategory;
import com.bingo.dict.model.SysDictData;
import com.bingo.dict.service.ISysDictCategoryService;
import com.bingo.dict.service.ISysDictDataService;
import com.bingo.dict.service.ISysDictService;
import com.bingo.study.common.component.dict.service.impl.AbstractDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author h-bingo
 * @Date 2023-08-17 14:24
 * @Version 1.0
 */
@ConditionalOnMissingBean(SysDictService.class)
public class SysDictService extends AbstractDictService<SysDictCategory, SysDictData> implements ISysDictService {

    @Autowired
    private ISysDictDataService sysDictDataService;

    @Autowired
    private ISysDictCategoryService sysDictCategoryService;

    @Override
    public List<SysDictData> getDictFromDb(String type) {
        return sysDictDataService.getSysDictData(type);
    }

    @Override
    public SysDictData getDictFromDb(String code, String type) {
        return sysDictDataService.getSysDictData(code, type);
    }

    @Override
    public Map<SysDictCategory, List<SysDictData>> getDictFromDb() {
        Map<SysDictCategory, List<SysDictData>> rtnMap = new HashMap<>();
        List<SysDictCategory> dictCategoryList = sysDictCategoryService.getSysDictCategory();
        for (SysDictCategory category : dictCategoryList) {
            rtnMap.put(category, this.getDictFromDb(category.getFdType()));
        }
        return rtnMap;
    }
}
