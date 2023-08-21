package com.bingo.study.common.core.web.model;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.bingo.study.common.core.web.interfaces.ITimeModel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author h-bingo
 * @Date 2023-08-21 15:42
 * @Version 1.0
 */
public abstract class TimeModel implements ITimeModel {

    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private Date beginTime;

    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private Date endTime;

    @Override
    public Date getBeginTime() {
        return beginTime;
    }

    @Override
    public void setBeginTime(Date beginTime) {
        this.beginTime = DateUtil.beginOfDay(beginTime);
    }

    @Override
    public Date getEndTime() {
        return endTime;
    }

    @Override
    public void setEndTime(Date endTime) {
        this.endTime = DateUtil.endOfDay(endTime);
    }
}
