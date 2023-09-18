package com.bingo.study.common.es.config;

import com.bingo.study.common.es.service.DefaultElasticSearchService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * @Author h-bingo
 * @Date 2023-09-18 11:09
 * @Version 1.0
 */
@Import(ElasticSearchConfig.class)
@ConditionalOnMissingBean(ElasticSearchAutoConfig.class)
public class ElasticSearchAutoConfig {

    @Bean
    @ConditionalOnMissingBean(DefaultElasticSearchService.class)
    public DefaultElasticSearchService defaultElasticSearchService() {
        return new DefaultElasticSearchService();
    }
}
