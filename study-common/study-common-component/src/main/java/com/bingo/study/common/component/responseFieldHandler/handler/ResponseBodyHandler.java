package com.bingo.study.common.component.responseFieldHandler.handler;

import org.springframework.core.Ordered;

/**
 * @Author h-bingo
 * @Date 2023-04-21 18:00
 * @Version 1.0
 */
public interface ResponseBodyHandler extends Ordered {

    boolean support(Object obj);

    /***
     * 排除的字段
     * @Param [obj, ignore]
     * @Return java.lang.Object
     * @Date 2023-08-10 09:55
     */
    Object ignore(Object obj, String[] ignore);

    /***
     * 保留的字段
     * @Param [obj, specify]
     * @Return java.lang.Object
     * @Date 2023-08-10 09:56
     */
    Object specify(Object obj, String[] specify);
}
