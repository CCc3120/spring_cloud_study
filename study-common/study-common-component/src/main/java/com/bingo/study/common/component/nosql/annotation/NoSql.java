package com.bingo.study.common.component.nosql.annotation;

import com.bingo.study.common.component.nosql.util.UpdateType;

import java.lang.annotation.*;

/**
 * 更新缓存注解，用在对应Class上
 * 1、index--需要更新es时必填
 * 2、type--需要更新es时必填
 * 3、collection--用于更新mongo，不填则默认取ClassName首字母小写
 *
 * @Author h-bingo
 * @Date 2023-01-11 09:32
 * @Version 1.0
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoSql {

    /**
     * es 索引名称
     */
    String index();

    /**
     * es type名称
     */
    String type();

    /**
     * mongo 集合名称 默认空取org.springframework.data.mongodb.core.mapping.Document的collection
     */
    String collection() default "";

    /**
     * 字段更新类型，默认更新非null的属性
     */
    UpdateType updateType() default UpdateType.NOT_NULL;
}

