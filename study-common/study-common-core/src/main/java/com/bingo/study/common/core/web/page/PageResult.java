package com.bingo.study.common.core.web.page;

import com.bingo.study.common.core.web.interfaces.IPageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName Page
 * @Description 分页数据对象
 * @Author h-bingo
 * @Date 2022-12-13 14:06
 * @Version 1.0
 */
@Data
@ApiModel("分页结果集")
public class PageResult<T> implements Serializable {

    @ApiModelProperty("数据列表")
    private List<T> dataList = Collections.emptyList();

    @ApiModelProperty("总数")
    private long count = 0L;

    @ApiModelProperty("页大小")
    private long pageSize;

    @ApiModelProperty("页码")
    private long pageNum;

    private PageResult(List<T> dataList, long count, long pageSize, long pageNum) {
        this.dataList = dataList;
        this.count = count;
        this.pageSize = pageSize;
        this.pageNum = pageNum;
    }

    private PageResult(long pageSize, long pageNum) {
        this.pageSize = pageSize;
        this.pageNum = pageNum;
    }

    public static <T> PageResult<T> of(List<T> dataList, long count, long pageSize, long pageNum) {
        return new PageResult<>(dataList, count, pageSize, pageNum);
    }

    public static <T> PageResult<T> of(List<T> dataList, long count, IPageModel model) {
        return of(dataList, count, model.getPageSize(), model.getPageNum());
    }

    public static <T> PageResult<T> of(IPageModel model) {
        return empty(model);
    }

    public static <T> PageResult<T> empty(IPageModel model) {
        return new PageResult<>(model.getPageSize(), model.getPageNum());
    }
}
