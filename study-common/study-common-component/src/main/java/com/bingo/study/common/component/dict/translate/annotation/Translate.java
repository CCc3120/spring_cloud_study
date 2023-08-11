package com.bingo.study.common.component.dict.translate.annotation;

import com.bingo.study.common.component.dict.translate.constant.TranslateType;
import com.bingo.study.common.core.enums.CodeDescEnum;

import java.lang.annotation.*;

/**
 * @Author h-bingo
 * @Date 2023-04-24 11:17
 * @Version 1.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Translate {

    /**
     * 翻译类型 {@link TranslateType}
     *
     * @return
     */
    TranslateType type();

    /**
     * 翻译填充的字段名，为空翻译结果覆盖注解标注的字段
     *
     * @return
     */
    String fillName() default "";

    /**
     * 枚举信息
     *
     * @return
     */
    Class<? extends CodeDescEnum> enumClass() default CodeDescEnum.class;

    /**
     * 字典类型
     *
     * @return
     */
    String dictType() default "";
}
