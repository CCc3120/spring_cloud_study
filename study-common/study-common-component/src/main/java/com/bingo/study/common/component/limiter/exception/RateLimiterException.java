package com.bingo.study.common.component.limiter.exception;

/**
 * @Author h-bingo
 * @Date 2023-04-26 10:35
 * @Version 1.0
 */
public class RateLimiterException extends RuntimeException {

    public RateLimiterException(String message) {
        super(message);
    }
}
