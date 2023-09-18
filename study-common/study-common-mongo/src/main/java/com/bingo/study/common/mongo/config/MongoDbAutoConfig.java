package com.bingo.study.common.mongo.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Import;

/**
 * @Author h-bingo
 * @Date 2023-09-18 11:15
 * @Version 1.0
 */
@Import(MongoDBConfig.class)
@ConditionalOnMissingBean(MongoDbAutoConfig.class)
public class MongoDbAutoConfig {
}
