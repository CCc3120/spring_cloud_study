package com.bingo.study.common.component.limiter.annotation;

import com.bingo.study.common.component.limiter.LimitRealize;
import com.bingo.study.common.component.limiter.LimitType;

import java.lang.annotation.*;

/**
 * @Author h-bingo
 * @Date 2023-04-26 09:26
 * @Version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {

    /**
     * 时间 单位: s
     *
     * @return
     */
    long time() default 60;

    /**
     * 请求次数
     *
     * @return
     */
    long count() default 100;

    /**
     * 限流类型
     *
     * @return
     */
    LimitType limitType() default LimitType.IP;

    /**
     * 限流实现方式
     *
     * @return
     */
    LimitRealize limitRealize() default LimitRealize.TOKEN_BUCKET;
}
