package com.bingo.test;

import com.bingo.study.common.component.httpLog.EnableHttpLogInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


// @EnableAspectJAutoProxy // 启用AOP，springboot自动装配所以不用加
// @EnableDynamicTransaction
// @EnableDynamicDB
@EnableHttpLogInterceptor
// @EnableRateLimiter
// @EnableRedisLock
// @EnableDeprecatedInterfaceSee
// @EnableResponseBodyHandler
// @ServletComponentScan
// @SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})
// @EnableRetry // 启用spring retry 功能
@SpringBootApplication
// @EnableMongoRepositories(basePackages = {"com.bingo.test.esAndMongo"})
public class StudySystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudySystemApplication.class, args);
    }
}
