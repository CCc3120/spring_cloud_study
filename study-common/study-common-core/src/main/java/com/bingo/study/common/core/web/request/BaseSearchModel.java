package com.bingo.study.common.core.web.request;

import com.bingo.study.common.core.web.interfaces.IOrderModel;
import com.bingo.study.common.core.web.interfaces.IPageModel;
import com.bingo.study.common.core.web.interfaces.ISearchModel;
import com.bingo.study.common.core.web.model.TimeModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName BaseSearchModel
 * @Description 查询model基类
 * @Author h-bingo
 * @Date 2022-12-13 14:32
 * @Version 1.0
 */
@Data
public class BaseSearchModel extends TimeModel implements IOrderModel, IPageModel, ISearchModel, Serializable {

    @ApiModelProperty("搜索内容")
    private String searchContent;

    @ApiModelProperty("搜索字段")
    private String searchField;

    @ApiModelProperty("搜索字段列表（需手动设置，若搜索字段为空，则取该字段）")
    private List<String> searchFieldList;

    @ApiModelProperty("排序类型 升序asc 降序desc")
    private String orderType = OrderType.ORDER_ASC.getCode();

    @ApiModelProperty("排序字段")
    private String orderField;

    @ApiModelProperty("页大小")
    private Long pageSize;

    @ApiModelProperty("页码")
    private Long pageNum;

    @ApiModelProperty("分页查询偏移量")
    private Long rowsOffset;

    @ApiModelProperty("请求时间戳")
    private Long timestamp;
}
