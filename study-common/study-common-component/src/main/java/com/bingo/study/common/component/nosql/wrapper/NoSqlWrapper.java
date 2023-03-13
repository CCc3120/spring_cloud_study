package com.bingo.study.common.component.nosql.wrapper;

import com.bingo.study.common.component.nosql.util.UpdateType;
import com.bingo.study.common.core.interfaces.IBaseModel;
import lombok.Data;

/**
 * 参数包装类
 *
 * @Author h-bingo
 * @Date 2023-01-10 17:39
 * @Version 1.0
 */
@Data
public class NoSqlWrapper {

    private IBaseModel model;

    /* es 的索引名称 */
    private String index;

    /* es的type */
    private String type;

    /* mongo的集合名称 */
    private String collection;

    /* 字段更新类型 */
    private UpdateType updateType;

}
