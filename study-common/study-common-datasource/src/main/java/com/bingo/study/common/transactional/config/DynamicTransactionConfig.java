package com.bingo.study.common.transactional.config;

import com.bingo.study.common.transactional.DefaultDynamicTransactionManager;
import com.bingo.study.common.transactional.DynamicTransactionManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @Author h-bingo
 * @Date 2023-05-12 10:08
 * @Version 1.0
 */
@ConditionalOnMissingBean(DynamicTransactionConfig.class)
public class DynamicTransactionConfig {

    @Bean
    @ConditionalOnMissingBean(DynamicTransactionManager.class)
    public DynamicTransactionManager dynamicTransactionManager() {
        return new DefaultDynamicTransactionManager();
    }
}
