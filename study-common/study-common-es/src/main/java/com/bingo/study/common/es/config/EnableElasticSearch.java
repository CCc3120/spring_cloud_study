package com.bingo.study.common.es.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author h-bingo
 * @Date 2023-09-18 10:30
 * @Version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ElasticSearchAutoConfig.class})
public @interface EnableElasticSearch {
}
