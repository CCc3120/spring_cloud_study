package com.bingo.study.common.core.web.interfaces;

import java.util.Date;

/**
 * @Author h-bingo
 * @Date 2023-08-21 15:28
 * @Version 1.0
 */
public interface ITimeModel extends IBaseWebModel {

    // @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    // private Date beginTime;
    //
    // @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    // private Date endTime;

    Date getBeginTime();

    void setBeginTime(Date beginTime);

    Date getEndTime();

    void setEndTime(Date endTime);
}
