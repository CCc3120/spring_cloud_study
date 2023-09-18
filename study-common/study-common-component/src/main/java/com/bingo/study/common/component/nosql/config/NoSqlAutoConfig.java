package com.bingo.study.common.component.nosql.config;

import com.bingo.study.common.component.nosql.service.EsUpdateService;
import com.bingo.study.common.component.nosql.service.MongoUpdateService;
import com.bingo.study.common.es.config.EnableElasticSearch;
import com.bingo.study.common.es.service.ElasticSearchService;
import com.bingo.study.common.mongo.config.EnableMongoDb;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * 通过自动装配实现
 *
 * @Author h-bingo
 * @Date 2023-01-12 16:52
 * @Version 1.0
 */
@EnableMongoDb
@EnableElasticSearch
@Configuration
public class NoSqlAutoConfig {

    @Bean
    @ConditionalOnMissingBean(EsUpdateService.class)
    public EsUpdateService esUpdateService(ElasticSearchService elasticSearchService) {
        EsUpdateService esUpdateService = new EsUpdateService();
        esUpdateService.setElasticSearchService(elasticSearchService);
        return esUpdateService;
    }

    @Bean
    @ConditionalOnMissingBean(MongoUpdateService.class)
    public MongoUpdateService mongoUpdateService(MongoTemplate mongoTemplate) {
        MongoUpdateService mongoUpdateService = new MongoUpdateService();
        mongoUpdateService.setMongoTemplate(mongoTemplate);
        return mongoUpdateService;
    }
}
