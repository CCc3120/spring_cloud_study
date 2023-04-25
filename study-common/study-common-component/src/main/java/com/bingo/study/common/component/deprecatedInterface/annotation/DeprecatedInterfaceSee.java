package com.bingo.study.common.component.deprecatedInterface.annotation;

import java.lang.annotation.*;

/**
 * @Author h-bingo
 * @Date 2023-04-23 15:55
 * @Version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DeprecatedInterfaceSee {

    /**
     * 提示
     *
     * @return
     */
    String value() default "";

    /**
     * FeignClient类
     *
     * @return
     */
    Class<?> feign() default DeprecatedInterfaceSee.class;

    /**
     * FeignClient类方法
     *
     * @return
     */
    String method() default "";
}
