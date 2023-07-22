package com.bingo.study.common.component.retry;

/**
 * @Author h-bingo
 * @Date 2023-06-28 09:54
 * @Version 1.0
 */
public @interface Retry {

    /**
     * 重试次数
     * @return
     */
    int count();
}
