package com.bingo.test;

import com.bingo.study.common.component.deprecatedInterface.annotation.EnableDeprecatedInterfaceSee;
import com.bingo.study.common.component.httpLog.EnableHttpLogInterceptor;
import com.bingo.study.common.component.limiter.annotation.EnableRateLimiter;
import com.bingo.study.common.component.lock.annotation.EnableRedisLock;
import com.bingo.study.common.component.responseFieldHandler.annotation.EnableResponseFieldHandler;
import com.bingo.study.common.datasource.annotation.EnableDynamicDB;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


// @EnableAspectJAutoProxy // 启用AOP，springboot自动装配所以不用加
// @EnableDynamicTransaction
@EnableDynamicDB
@EnableHttpLogInterceptor
@EnableRateLimiter
@EnableRedisLock
@EnableDeprecatedInterfaceSee
@EnableResponseFieldHandler
// @ServletComponentScan
// @SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})
// @EnableRetry // 启用spring retry 功能
@SpringBootApplication
public class StudySystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudySystemApplication.class, args);
    }
}
