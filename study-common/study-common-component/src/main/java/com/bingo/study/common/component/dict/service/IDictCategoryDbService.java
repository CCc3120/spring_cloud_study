package com.bingo.study.common.component.dict.service;

import com.bingo.study.common.core.dict.IDictCategoryModel;

import java.util.List;

/**
 * 业务字典类型数据需要实现改接口
 *
 * @Author h-bingo
 * @Date 2023-09-06 15:36
 * @Version 1.0
 */
public interface IDictCategoryDbService<C extends IDictCategoryModel> {

    /***
     * 查询所有字典类别
     * @Param []
     * @Return java.util.List<C>
     * @Date 2023-09-06 15:38
     */
    List<C> getDictCategoryFromDb();
}
