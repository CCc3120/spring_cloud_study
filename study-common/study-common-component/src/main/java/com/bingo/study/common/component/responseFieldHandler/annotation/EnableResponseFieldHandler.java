package com.bingo.study.common.component.responseFieldHandler.annotation;

import com.bingo.study.common.component.responseFieldHandler.DefaultResponseBodyWrapperFilter;
import com.bingo.study.common.component.responseFieldHandler.handler.DefaultResponseBodyHandler;
import com.bingo.study.common.component.responseFieldHandler.handler.ListResponseBodyHandler;
import com.bingo.study.common.component.responseFieldHandler.handler.PageResultResponseBodyHandler;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启 {@link DefaultResponseBodyWrapperFilter} 和 {@link ResponseField} 功能
 * 同时在相应的 controller 或 请求方法加上 {@link ResponseField} 注解可自定义功能，否则使用默认逻辑
 *
 * @Author h-bingo
 * @Date 2023-04-23 15:43
 * @Version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({DefaultResponseBodyHandler.class, ListResponseBodyHandler.class, PageResultResponseBodyHandler.class,
        DefaultResponseBodyWrapperFilter.class})
public @interface EnableResponseFieldHandler {
}
