package com.bingo.study.common.mongo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;

/**
 * 此类若不加，那么插入的一行会默认添加一个_class字段来存储实体类类型 如（com.bingo.springmongodb.model.Student）
 * <p>
 * { "_id" : "fd180ba65f034ed58770c5311f74c72d", "fdName" : "张三", "fdClass" : "幼稚园大班", "fdAge" : 18, "fdBirthday" :
 * ISODate("2022-07-25T08:57:57.020Z"),
 * "_class" : "com.bingo.springmongodb.model.Student" }
 * <p>
 * mongo新增数据时，主键id必须有值，不然插入后会生成ObjectId（）类型主键
 * { "_id" : ObjectId("637dec74f229832003649bc2"), "fdName" : "张三三", "fdBirthday" : ISODate("2022-11-23T09:48:36
 * .892Z") }
 */
@Configuration
@ConditionalOnBean(MongoTemplate.class)
@ConditionalOnMissingBean(MongoDBReadyListener.class)
public class MongoDBReadyListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String TYPEKEY = "_class";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        MongoConverter converter = mongoTemplate.getConverter();
        if (converter.getTypeMapper().isTypeKey(TYPEKEY)) {
            ((MappingMongoConverter) converter).setTypeMapper(new DefaultMongoTypeMapper(null));
        }
    }
}
