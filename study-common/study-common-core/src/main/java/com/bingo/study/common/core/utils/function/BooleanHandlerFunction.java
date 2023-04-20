package com.bingo.study.common.core.utils.function;

/**
 * @Author h-bingo
 * @Date 2023-04-13 13:47
 * @Version 1.0
 */
@FunctionalInterface
public interface BooleanHandlerFunction {

    void booleanHandler(Runnable trueHandler, Runnable falseHandler);
}
