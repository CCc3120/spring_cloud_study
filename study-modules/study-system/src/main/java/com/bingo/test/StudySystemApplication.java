package com.bingo.test;

import com.bingo.study.common.component.deprecatedInterface.annotation.EnableDeprecatedInterfaceSee;
import com.bingo.study.common.component.limiter.annotation.EnableRateLimiter;
import com.bingo.study.common.component.lock.annotation.EnableRedisLock;
import com.bingo.study.common.component.returnValue.annotation.EnableReturnValue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@EnableRateLimiter
@EnableRedisLock
@EnableDeprecatedInterfaceSee
@EnableReturnValue
@SpringBootApplication
public class StudySystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudySystemApplication.class, args);
    }

}
