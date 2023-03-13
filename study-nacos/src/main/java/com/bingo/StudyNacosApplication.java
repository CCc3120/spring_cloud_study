package com.bingo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(value = {"com.bingo"})
@EnableDiscoveryClient(autoRegister = true)
@SpringBootApplication
public class StudyNacosApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyNacosApplication.class, args);
    }

}
