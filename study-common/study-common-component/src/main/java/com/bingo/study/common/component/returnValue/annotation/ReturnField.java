package com.bingo.study.common.component.returnValue.annotation;

import com.bingo.study.common.component.returnValue.IgnoreField;

import java.lang.annotation.*;

/**
 * @Author h-bingo
 * @Date 2023-04-21 17:23
 * @Version 1.0
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReturnField {
    /**
     * 是否启用
     * <p>
     * 启用注解 {@link EnableReturnValue} 后，则默认去除 {@link IgnoreField} 中的字段
     * 若需要禁用，则设置 {@link ReturnField#enable()} 为false
     *
     * @Return boolean
     * @Date 2023-04-21 17:24
     */
    boolean enable() default true;

    /**
     * 忽略的字段属性，优先级低于{@link ReturnField#ignore()}
     *
     * @Return java.lang.String[]
     * @Date 2023-04-21 17:25
     */
    String[] ignore() default {};

    /**
     * 保留的属性，优先级高于{@link ReturnField#ignore()}
     * <p>
     * {@link ReturnField#ignore()}和{@link ReturnField#specify()}都存在则{@link ReturnField#ignore()}失效
     *
     * @Return java.lang.String[]
     * @Date 2023-04-21 17:25
     */
    String[] specify() default {};
}
