package com.bingo.study.common.core.web.interfaces;

/**
 * 分页查询接口
 *
 * @Author h-bingo
 * @Date 2023-08-21 11:00
 * @Version 1.0
 */
public interface IPageModel extends IBaseWebModel {

    long DEFAULT_PAGE_SIZE = 10;
    long DEFAULT_PAGE_NUM = 1;

    // private Long pageSize;

    // private Long pageNum;

    Long getPageSize();

    void setPageSize(Long pageSize);

    Long getPageNum();

    void setPageNum(Long pageNum);

    // private Long rowsOffset;

    Long getRowsOffset();

    void setRowsOffset(Long rowsOffset);
}
