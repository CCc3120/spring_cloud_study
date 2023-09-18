package com.bingo.study.common.mongo.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author h-bingo
 * @Date 2023-09-18 10:24
 * @Version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({MongoDbAutoConfig.class})
public @interface EnableMongoDb {
}
