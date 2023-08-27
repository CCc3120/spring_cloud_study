package com.bingo.dict.config;

import com.bingo.dict.cache.SysDictCacheService;
import com.bingo.dict.service.impl.SysDictCategoryServiceImpl;
import com.bingo.dict.service.impl.SysDictDataServiceImpl;
import com.bingo.dict.service.impl.SysDictService;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Author h-bingo
 * @Date 2023-08-17 14:58
 * @Version 1.0
 */
@Configuration
@ConditionalOnMissingBean(SysDictAutoConfig.class)
@Import({SysDictCacheService.class,
        SysDictDataServiceImpl.class,
        SysDictCategoryServiceImpl.class,
        SysDictService.class,
        ControllerRegisterConfiguration.class})
// @MapperScan("com.bingo.dict.dao")
public class SysDictAutoConfig {

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.bingo.dict");
        return mapperScannerConfigurer;
    }
}
