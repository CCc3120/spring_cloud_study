package com.bingo.study.common.component.nosql.config;

import com.bingo.study.common.component.nosql.service.EsUpdateService;
import com.bingo.study.common.component.nosql.service.MongoUpdateService;
import com.bingo.study.common.es.service.ElasticSearchService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @Author h-bingo
 * @Date 2023-01-12 16:52
 * @Version 1.0
 */
@Configuration
public class NoSqlAutoConfig {

    @Bean
    @ConditionalOnClass({ElasticSearchService.class})
    public EsUpdateService esUpdateService(ElasticSearchService elasticSearchService) {
        EsUpdateService esUpdateService = new EsUpdateService();
        esUpdateService.setElasticSearchService(elasticSearchService);
        return esUpdateService;
    }

    @Bean
    @ConditionalOnBean({MongoTemplate.class})
    public MongoUpdateService mongoUpdateService(MongoTemplate mongoTemplate) {
        MongoUpdateService mongoUpdateService = new MongoUpdateService();
        mongoUpdateService.setMongoTemplate(mongoTemplate);
        return mongoUpdateService;
    }
}
