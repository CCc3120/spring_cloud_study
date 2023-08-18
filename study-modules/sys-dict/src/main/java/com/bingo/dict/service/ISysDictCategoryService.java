package com.bingo.dict.service;

import com.bingo.dict.model.SysDictCategory;

import java.util.List;

/**
 * @Author h-bingo
 * @Date 2023-08-17 14:27
 * @Version 1.0
 */
public interface ISysDictCategoryService {

    /***
     * 查询所有字典类别
     * @Param []
     * @Return java.util.List<com.bingo.dict.model.SysDictCategory>
     * @Date 2023-08-17 14:51
     */
    List<SysDictCategory> getSysDictCategory();
}
