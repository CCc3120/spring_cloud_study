package com.bingo.study.common.component.returnValue.annotation;

import com.bingo.study.common.component.returnValue.ReturnValueAdvice;
import com.bingo.study.common.component.returnValue.handler.DefaultValueHandler;
import com.bingo.study.common.component.returnValue.handler.ListValueHandler;
import com.bingo.study.common.component.returnValue.handler.PageValueHandler;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启 {@link ReturnValueAdvice} 和 {@link ReturnField} 功能
 * 同时在相应的 controller 或 请求方法加上 {@link ReturnField} 注解可自定义功能，否则使用默认逻辑
 *
 * @Author h-bingo
 * @Date 2023-04-23 15:43
 * @Version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({DefaultValueHandler.class, ListValueHandler.class, PageValueHandler.class, ReturnValueAdvice.class})
public @interface EnableReturnValue {
}
