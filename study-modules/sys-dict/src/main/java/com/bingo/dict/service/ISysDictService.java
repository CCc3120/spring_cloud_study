package com.bingo.dict.service;

import com.bingo.dict.model.SysDictCategory;
import com.bingo.dict.model.SysDictData;
import com.bingo.study.common.component.dict.service.IDictService;

import java.util.List;
import java.util.Map;

/**
 * @Author h-bingo
 * @Date 2023-08-17 14:44
 * @Version 1.0
 */
public interface ISysDictService extends IDictService<SysDictCategory, SysDictData> {

    /***
     * 查询数据库
     * 查询 指定 type 字典
     * @Param [type]
     * @Return java.util.List<D>
     * @Date 2023-08-11 09:32
     */
    List<SysDictData> getDictFormDb(String type);

    /***
     * 查询数据库
     * 精准查询字典
     * @Param [code, type]
     * @Return M
     * @Date 2023-08-10 16:17
     */
    SysDictData getDictFormDb(String code, String type);

    /***
     * 查询数据库
     * 查询全部字典
     * @Param []
     * @Return java.util.Map<C, java.util.List < D>>
     * @Date 2023-08-11 09:33
     */
    Map<SysDictCategory, List<SysDictData>> getDictFormDb();
}
