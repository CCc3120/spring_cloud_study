package com.bingo.study.common.component.lock;

/**
 * @Author h-bingo
 * @Date 2023-04-25 10:41
 * @Version 1.0
 */
public interface RedisLockCallBack {

    Object doWork() throws Throwable;
}
