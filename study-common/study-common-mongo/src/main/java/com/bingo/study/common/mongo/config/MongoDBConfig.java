package com.bingo.study.common.mongo.config;

import com.bingo.study.common.mongo.config.properties.MongoDBProperties;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.MongoDriverInformation;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.internal.MongoClientImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import java.util.Collections;

/**
 * @Author h-bingo
 * @Date 2023-04-23 11:48
 * @Version 1.0
 */
@Configuration
@Import(MongoDBProperties.class)
@ConditionalOnMissingBean(MongoDBConfig.class)
@AutoConfigureBefore(MongoAutoConfiguration.class)
public class MongoDBConfig {

    @Autowired
    private MongoDBProperties mongoDBProperties;

    @Bean
    public MongoClient mongoClient() {
        // Set credentials
        MongoCredential credential = MongoCredential.createCredential(mongoDBProperties.getUsername(),
                mongoDBProperties.getDatabase(), mongoDBProperties.getPassword().toCharArray());
        ServerAddress serverAddress = new ServerAddress(mongoDBProperties.getHost(), mongoDBProperties.getPort());
        // Mongo Client
        MongoDriverInformation info = MongoDriverInformation.builder().build();
        MongoClientSettings build = MongoClientSettings.builder()
                .applyToClusterSettings(builder -> builder.hosts(Collections.singletonList(serverAddress)))
                .credential(credential)
                .build();
        return new MongoClientImpl(build, info);
    }

    @Bean
    public MongoDatabaseFactory mongoDbFactory(MongoClient mongoClient) {
        return new SimpleMongoClientDatabaseFactory(mongoClient, mongoDBProperties.getDatabase());
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDatabaseFactory) {
        return new MongoTemplate(mongoDatabaseFactory);
    }
}
