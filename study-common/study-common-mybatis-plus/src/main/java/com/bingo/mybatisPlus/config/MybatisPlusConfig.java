package com.bingo.mybatisPlus.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author h-bingo
 * @Date 2023-03-01 15:25
 * @Version 1.0
 */
@Configuration
@ConditionalOnMissingBean(MybatisPlusConfig.class)
public class MybatisPlusConfig {

    /**
     * 新增分页拦截器，并设置数据库类型为mysql
     * <p>
     * 默认mysql，后续改成配置
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
