package com.bingo.study.common.datasource.annotation;

import com.bingo.study.common.datasource.aspect.DynamicDBAspect;
import com.bingo.study.common.datasource.config.DynamicDBConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用 {@link DynamicDB} 注解功能
 *
 * @Author h-bingo
 * @Date 2023-04-25 17:35
 * @Version 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({DynamicDBAspect.class, DynamicDBConfig.class})
public @interface EnableDynamicDB {
}

