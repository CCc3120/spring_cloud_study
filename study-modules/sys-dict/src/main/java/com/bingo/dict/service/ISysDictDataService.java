package com.bingo.dict.service;

import com.bingo.dict.model.SysDictData;

import java.util.List;

/**
 * @Author h-bingo
 * @Date 2023-08-17 14:27
 * @Version 1.0
 */
public interface ISysDictDataService {

    /***
     * 查询 指定 type 字典
     * @Param [type]
     * @Return java.util.List<com.bingo.dict.model.SysDictData>
     * @Date 2023-08-17 14:50
     */
    List<SysDictData> getSysDictData(String type);

    /***
     * 精准查询字典
     * @Param [code, type]
     * @Return com.bingo.dict.model.SysDictData
     * @Date 2023-08-17 14:50
     */
    SysDictData getSysDictData(String code, String type);
}
