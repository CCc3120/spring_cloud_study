package com.bingo.study.common.core.page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName BaseSearchModel
 * @Description 查询model基类
 * @Author h-bingo
 * @Date 2022-12-13 14:32
 * @Version 1.0
 */
@Data
public class BaseSearchModel implements Serializable {

    public static final String ASC = "asc";
    public static final String DESC = "desc";
    public static final long PAGE_SIZE = 10;
    public static final long PAGE_NUM = 1;

    @ApiModelProperty("搜索内容")
    private String searchContent;

    @ApiModelProperty("搜索字段")
    private String searchProp;

    @ApiModelProperty("排序类型 升序asc 降序desc")
    private String orderType = ASC;

    @ApiModelProperty("排序字段")
    private String orderProp;

    @ApiModelProperty("页大小")
    private long pageSize = PAGE_SIZE;

    @ApiModelProperty("页码")
    private long pageNum = PAGE_NUM;
}
