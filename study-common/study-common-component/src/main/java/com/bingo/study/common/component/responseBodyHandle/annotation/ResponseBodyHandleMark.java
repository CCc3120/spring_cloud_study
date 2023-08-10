package com.bingo.study.common.component.responseBodyHandle.annotation;

import com.bingo.study.common.component.responseBodyHandle.IgnoreField;

import java.lang.annotation.*;

/**
 * @Author h-bingo
 * @Date 2023-04-21 17:23
 * @Version 1.0
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseBodyHandleMark {
    /**
     * 是否启用
     * <p>
     * 启用注解 {@link EnableResponseBodyHandler}
     * true 则去除 {@link IgnoreField} 中的字段
     * false 则不启用该功能
     *
     * @Return boolean
     * @Date 2023-04-21 17:24
     */
    boolean filedFilter() default true;

    /**
     * 是否启用
     * <p>
     * 启用注解 {@link EnableResponseBodyHandler} 后，则默认把返回结果进行统一包装
     * 若需要禁用，则设置 {@link ResponseBodyHandleMark#wrapper()} 为 false
     *
     * @Return boolean
     * @Date 2023-04-21 17:24
     */
    boolean wrapper() default true;

    /**
     * 忽略的字段属性，优先级低于 {@link ResponseBodyHandleMark#specify()}
     *
     * @Return java.lang.String[]
     * @Date 2023-04-21 17:25
     */
    String[] ignore() default {};

    /**
     * 保留的属性，优先级高于{@link ResponseBodyHandleMark#ignore()}
     * <p>
     * {@link ResponseBodyHandleMark#ignore()} 和 {@link ResponseBodyHandleMark#specify()}都存在
     * 则 {@link ResponseBodyHandleMark#ignore()} 失效
     *
     * @Return java.lang.String[]
     * @Date 2023-04-21 17:25
     */
    String[] specify() default {};
}
