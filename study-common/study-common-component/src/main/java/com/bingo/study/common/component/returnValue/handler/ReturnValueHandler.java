package com.bingo.study.common.component.returnValue.handler;

import org.springframework.core.Ordered;

/**
 * @Author h-bingo
 * @Date 2023-04-21 18:00
 * @Version 1.0
 */
public interface ReturnValueHandler extends Ordered {

    boolean support(Object obj);

    Object ignore(Object obj, String[] ignore);

    Object specify(Object obj, String[] specify);
}
