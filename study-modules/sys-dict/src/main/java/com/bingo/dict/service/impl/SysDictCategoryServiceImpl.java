package com.bingo.dict.service.impl;

import com.bingo.dict.model.SysDictCategory;
import com.bingo.dict.service.ISysDictCategoryService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import java.util.List;

/**
 * @Author h-bingo
 * @Date 2023-08-17 14:27
 * @Version 1.0
 */
@ConditionalOnMissingBean(SysDictCategoryServiceImpl.class)
public class SysDictCategoryServiceImpl implements ISysDictCategoryService {
    @Override
    public List<SysDictCategory> getSysDictCategory() {
        return null;
    }
}
