package com.bingo.study.common.component.dict.service;

import com.bingo.study.common.core.dict.IDictDataModel;

import java.util.List;

/**
 * 业务字典数据需要实现改接口
 *
 * @Author h-bingo
 * @Date 2023-09-06 15:36
 * @Version 1.0
 */
public interface IDictDataDbService<D extends IDictDataModel> {

    /***
     * 查询 指定 type 字典
     * @Param [type]
     * @Return List<D>
     * @Date 2023-09-06 15:37
     */
    List<D> getDictDataFromDb(String type);

    /***
     * 精准查询字典数据
     * @Param [code, type]
     * @Return D
     * @Date 2023-09-06 15:37
     */
    D getDictDataFromDb(String code, String type);
}
