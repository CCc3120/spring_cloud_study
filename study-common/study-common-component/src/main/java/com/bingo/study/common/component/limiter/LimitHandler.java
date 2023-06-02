package com.bingo.study.common.component.limiter;

import com.bingo.study.common.component.limiter.annotation.RateLimiter;
import org.aspectj.lang.JoinPoint;

/**
 * @Author h-bingo
 * @Date 2023-06-02 11:27
 * @Version 1.0
 */
public interface LimitHandler {

    void limit(JoinPoint point, RateLimiter limiter);
}
