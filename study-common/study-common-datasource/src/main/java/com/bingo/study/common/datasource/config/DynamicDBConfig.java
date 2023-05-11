package com.bingo.study.common.datasource.config;

import com.bingo.study.common.datasource.DynamicDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @Author h-bingo
 * @Date 2023-04-25 17:50
 * @Version 1.0
 */

// @Configuration
// @ConditionalOnClass(DruidDataSource.class)
// @AutoConfigureBefore(DataSourceAutoConfiguration.class)
// @EnableConfigurationProperties({DruidStatProperties.class, DataSourceProperties.class})
// @Import({DruidSpringAopConfiguration.class,
//         DruidStatViewServletConfiguration.class,
//         DruidWebStatFilterConfiguration.class,
//         DruidFilterConfiguration.class})
// @Configuration
@Import({DynamicDataSource.class})
@ConditionalOnMissingBean({DynamicDBConfig.class})
public class DynamicDBConfig {

    /**
     * 配置多数据源后IOC中存在多个数据源了，事务管理器需要重新配置，不然器不知道选择哪个数据源
     * 事务管理器此时管理的数据源将是动态数据源dynamicDataSource
     * 配置@Transactional注解
     *
     * @return
     */
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
