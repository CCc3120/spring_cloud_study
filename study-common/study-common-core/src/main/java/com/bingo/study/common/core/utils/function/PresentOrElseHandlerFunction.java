package com.bingo.study.common.core.utils.function;

import java.util.function.Consumer;

/**
 * @Author h-bingo
 * @Date 2023-04-13 13:58
 * @Version 1.0
 */
@FunctionalInterface
public interface PresentOrElseHandlerFunction<T> {

    void presentOrElseHandler(Consumer<? super T> action, Runnable emptyAction);
}
