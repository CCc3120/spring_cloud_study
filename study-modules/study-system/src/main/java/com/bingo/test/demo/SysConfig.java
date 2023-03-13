package com.bingo.test.demo;

import com.bingo.study.common.component.test.nosql.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author h-bingo
 * @Date 2023-01-29 17:02
 * @Version 1.0
 */
@Configuration
public class SysConfig {

    @Bean
    public Student student(String param1, String param2){
        Student student = null;
        try {
            // 这里因为参数不对报错了
            student = new Student();

        } catch (Exception e) {
            return null;
        }
        return student;
    }
}
