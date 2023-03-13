package com.bingo.study.common.component.nosql.service;

import com.bingo.study.common.core.interfaces.IBaseModel;

/**
 * @Author h-bingo
 * @Date 2023-01-09 17:20
 * @Version 1.0
 */
public interface NoSqlService {

    /**
     * 数据更新/新增
     *
     * @param model
     * @return
     */
    boolean update(IBaseModel model);

    /**
     * 删除数据
     *
     * @param model
     * @return
     */
    boolean delete(IBaseModel model);
}
