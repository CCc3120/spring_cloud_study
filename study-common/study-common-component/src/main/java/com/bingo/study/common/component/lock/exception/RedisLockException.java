package com.bingo.study.common.component.lock.exception;

/**
 * @Author h-bingo
 * @Date 2023-04-25 09:30
 * @Version 1.0
 */
public class RedisLockException extends RuntimeException {

    public RedisLockException(String message) {
        super(message);
    }
}
