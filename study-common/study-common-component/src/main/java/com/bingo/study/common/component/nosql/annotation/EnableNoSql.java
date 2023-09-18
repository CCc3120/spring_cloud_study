package com.bingo.study.common.component.nosql.annotation;

import com.bingo.study.common.component.nosql.config.NoSqlAutoConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author h-bingo
 * @Date 2023-09-18 12:50
 * @Version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(NoSqlAutoConfig.class)
public @interface EnableNoSql {
}
