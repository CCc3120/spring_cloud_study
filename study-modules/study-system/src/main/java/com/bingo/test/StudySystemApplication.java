package com.bingo.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;


// @EnableAspectJAutoProxy // 启用AOP，springboot自动装配所以不用加
// @EnableDynamicTransaction
// @EnableDynamicDB
// @EnableHttpLogInterceptor
// @EnableRateLimiter
// @EnableRedisLock
// @EnableTranslate
// @EnableDeprecatedInterfaceSee
// @EnableReturnValue
// @ServletComponentScan
// @SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})
// @EnableRetry // 启用spring retry 功能
@SpringBootApplication
public class StudySystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudySystemApplication.class, args);
    }

}
