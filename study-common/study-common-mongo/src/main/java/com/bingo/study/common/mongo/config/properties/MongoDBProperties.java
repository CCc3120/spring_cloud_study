package com.bingo.study.common.mongo.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author h-bingo
 * @Date 2023-04-23 14:26
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = "mongodb")
public class MongoDBProperties {

    private String host;

    private int port;

    private String username;

    private String password;

    private String database;

    private String authDb;
}
