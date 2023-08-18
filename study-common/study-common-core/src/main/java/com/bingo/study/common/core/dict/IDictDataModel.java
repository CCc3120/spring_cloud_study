package com.bingo.study.common.core.dict;

/**
 * @Author h-bingo
 * @Date 2023-06-13 09:33
 * @Version 1.0
 */
public interface IDictDataModel extends IDictCategoryModel {

    /**
     * 字典编码
     *
     * @return
     */
    String getFdCode();

    /**
     * 字典名称
     *
     * @return
     */
    String getFdName();
}
