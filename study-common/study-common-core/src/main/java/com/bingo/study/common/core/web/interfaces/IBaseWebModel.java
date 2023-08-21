package com.bingo.study.common.core.web.interfaces;

import java.io.Serializable;

/**
 * @Author h-bingo
 * @Date 2023-08-21 09:56
 * @Version 1.0
 */
public interface IBaseWebModel extends Serializable {

    // private Long timestamp;

    Long getTimestamp();

    void setTimestamp(Long timestamp);
}
