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
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;

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
                mongoDBProperties.getAuthDb(), mongoDBProperties.getPassword().toCharArray());
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
    public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDatabaseFactory, MongoConverter mongoConverter) {
        mongoConverterHandle(mongoConverter);
        return new MongoTemplate(mongoDatabaseFactory, mongoConverter);
    }

    private static final String MONGO_CLASS_KEY = "_class";

    /**
     * 此类方法不加，那么插入的一行会默认添加一个_class字段来存储实体类类型 如（com.bingo.springmongodb.model.Student）
     * <p>
     * { "_id" : "fd180ba65f034ed58770c5311f74c72d", "fdName" : "张三", "fdClass" : "幼稚园大班", "fdAge" : 18, "fdBirthday" :
     * ISODate("2022-07-25T08:57:57.020Z"),
     * "_class" : "com.bingo.springmongodb.model.Student" }
     * <p>
     * mongo新增数据时，主键id必须有值，不然插入后会生成ObjectId（）类型主键
     * { "_id" : ObjectId("637dec74f229832003649bc2"), "fdName" : "张三三", "fdBirthday" : ISODate("2022-11-23T09:48:36
     * .892Z") }
     */
    private void mongoConverterHandle(MongoConverter mongoConverter) {
        if (mongoConverter.getTypeMapper().isTypeKey(MONGO_CLASS_KEY)) {
            ((MappingMongoConverter) mongoConverter).setTypeMapper(new DefaultMongoTypeMapper(null));
        }
    }
}
