package com.bingo.study.common.core.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Jackson 工具类
 *
 * @author bingo
 * @date 2022-04-29 16:01
 */
@Slf4j
public class JsonMapper {

    private static ObjectMapper objectMapper;

    private static final JsonMapper jsonMapper = new JsonMapper();

    private JsonMapper() {
        objectMapper = new ObjectMapper();
        objectMapper
                // true - 遇到没有的属性就报错 false - 没有的属性不会管，不会报错
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                // 转换为格式化的json 显示出来的格式美化
                .configure(SerializationFeature.INDENT_OUTPUT, true)
                // .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
                // .configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true)
                // 序列化的时候序列对象的那些属性
                // JsonInclude.Include.NON_DEFAULT 属性为默认值不序列化
                // JsonInclude.Include.ALWAYS      所有属性
                // JsonInclude.Include.NON_EMPTY   属性为 空（“”） 或者为 NULL 都不序列化
                // JsonInclude.Include.NON_NULL    属性为NULL 不序列化
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        // 序列化日期格式
        // .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        // .setDateFormat(new SimpleDateFormat(DateUtil.DEFAULT_PATTERN))
        // 处理不同的时区偏移格式
        // .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        // .registerModule(new JavaTimeModule())
        ;
    }

    public static JsonMapper getInstance() {
        return jsonMapper;
    }

    public <T> String toJsonString(T t) {
        String result = null;
        try {
            result = objectMapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            log.warn("Object to JsonString Exception", e);
        }
        return result;
    }

    public <T> T fromJson(String jsonStr, Class<T> t) {
        if (StringUtil.isNull(jsonStr))
            return null;

        T result = null;
        try {
            result = objectMapper.readValue(jsonStr, t);
        } catch (Exception e) {
            log.warn("JsonString to Object Exception", e);
        }
        return result;
    }

    public <T> List<T> fromJson(String jsonStr, Class<? extends Collection> cls, Class<T> t) {
        if (StringUtil.isNull(jsonStr))
            return null;

        List<T> result = null;
        try {
            JavaType type = objectMapper.getTypeFactory().constructCollectionType(cls, t);
            result = objectMapper.readValue(jsonStr, type);
        } catch (IOException e) {
            log.warn("JsonString to List Exception", e);
        }
        return result;
    }

    public <K, V> Map<K, V> fromJson(String jsonStr, Class<? extends Map> cls, Class<K> key, Class<V> value) {
        if (StringUtil.isNull(jsonStr))
            return null;

        Map<K, V> result = null;
        try {
            JavaType type = objectMapper.getTypeFactory().constructMapType(cls, key, value);
            result = objectMapper.readValue(jsonStr, type);
        } catch (IOException e) {
            log.warn("JsonString to Map Exception", e);
        }
        return result;
    }
}
