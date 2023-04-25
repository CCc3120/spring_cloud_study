package com.bingo.study.common.swagger.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author h-bingo
 * @Date 2023-02-24 15:35
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {

    private boolean enable;

    private String title;

    private String description;

    private String author;

    private String url;

    private String email;

    private String version;
}
