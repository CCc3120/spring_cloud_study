package com.bingo.study.common.datasource.annotation;

import com.bingo.study.common.datasource.DynamicDBType;

import java.lang.annotation.*;

/**
 * 指定使用数据源
 *
 * @Author h-bingo
 * @Date 2023-04-25 16:50
 * @Version 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicDB {

    /**
     * 数据源
     *
     * @return
     */
    DynamicDBType value() default DynamicDBType.MASTER;
}
