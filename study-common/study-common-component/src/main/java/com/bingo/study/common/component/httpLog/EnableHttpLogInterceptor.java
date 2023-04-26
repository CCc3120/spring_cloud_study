package com.bingo.study.common.component.httpLog;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author h-bingo
 * @Date 2023-04-26 15:24
 * @Version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({HttpLogHandlerAutoConfig.class})
public @interface EnableHttpLogInterceptor {
}
