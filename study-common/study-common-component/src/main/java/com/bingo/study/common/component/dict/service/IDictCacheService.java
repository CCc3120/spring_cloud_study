package com.bingo.study.common.component.dict.service;

import com.bingo.study.common.core.dict.IDictDataModel;
import com.bingo.study.common.core.dict.IDictCategoryModel;

import java.util.List;
import java.util.Map;

/**
 * 字典缓存接口，默认走redis
 *
 * @Author h-bingo
 * @Date 2023-08-10 15:46
 * @Version 1.0
 */
public interface IDictCacheService<C extends IDictCategoryModel, D extends IDictDataModel>
        extends IDictTranslateService<D> {

    /***
     * 设置字典缓存
     * @Param [dictMap]
     * @Return void
     * @Date 2023-08-10 16:17
     */
    void setDict(Map<C, List<D>> dictMap);

    /***
     * 设置 指定 type 字典缓存
     * @Param [type, dictList]
     * @Return void
     * @Date 2023-08-10 17:36
     */
    void setDict(C type, List<D> dictList);

    /***
     * 查询 指定 type 字典
     * @Param [type]
     * @Return java.util.List<D>
     * @Date 2023-08-11 09:32
     */
    List<D> getDict(String type);

    /***
     * 精准查询字典缓存
     * @Param [code, type]
     * @Return M
     * @Date 2023-08-10 16:17
     */
    D getDict(String code, String type);

    /***
     * 查询全部字典
     * @Param []
     * @Return java.util.Map<C, java.util.List < D>>
     * @Date 2023-08-11 09:33
     */
    Map<C, List<D>> getDict();

    /***
     * 移除全部字典缓存
     * @Param []
     * @Return void
     * @Date 2023-08-10 16:17
     */
    void removeDict();

    /***
     * 移除指定 type 字典缓存
     * @Param [type]
     * @Return void
     * @Date 2023-08-10 16:17
     */
    void removeDict(String type);

    /***
     * 刷新指定 type 的字典
     * @Param [type, dictList]
     * @Return void
     * @Date 2023-08-10 16:17
     */
    void refreshDict(C type, List<D> dictList);

    /***
     * 批量刷新字典
     * @Param [dictMap]
     * @Return void
     * @Date 2023-08-10 16:18
     */
    void refreshDict(Map<C, List<D>> dictMap);
}
