package com.bingo.study.common.component.nosql.util;

import com.bingo.study.common.component.nosql.annotation.NoSql;
import com.bingo.study.common.component.nosql.exception.NoSqlException;
import com.bingo.study.common.component.nosql.wrapper.NoSqlWrapper;
import com.bingo.study.common.core.interfaces.IBaseModel;
import com.bingo.study.common.core.utils.ObjectUtil;
import com.bingo.study.common.core.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author h-bingo
 * @Date 2023-01-11 09:56
 * @Version 1.0
 */
@Slf4j
public class NoSqlUtil {

    public static NoSqlWrapper build(IBaseModel model) {
        NoSqlWrapper wrapper = new NoSqlWrapper();
        // NoSql
        NoSql noSql = AnnotationUtils.findAnnotation(model.getClass(), NoSql.class);
        if (noSql == null) {
            log.warn("{} Class not found @NoSql, please check it", model.getClass().getTypeName());
            throw new NoSqlException(model.getClass());
        }

        wrapper.setIndex(noSql.index());
        wrapper.setType(noSql.type());
        wrapper.setModel(model);
        wrapper.setUpdateType(noSql.updateType());
        wrapper.setCollection(noSql.collection());
        if (StringUtil.isNull(wrapper.getCollection())) {
            Document document = AnnotationUtils.findAnnotation(model.getClass(), Document.class);
            String collection;
            if (document != null) {
                collection = StringUtil.isNull(document.collection()) ?
                        StringUtil.humpToUnderline(model.getClass().getSimpleName()) : document.collection();
            } else {
                collection = StringUtil.humpToUnderline(model.getClass().getSimpleName());
            }
            wrapper.setCollection(collection);
        }
        return wrapper;
    }

    public static void buildUpdate(Update update, NoSqlWrapper wrapper) {
        Set<Field> fields = new HashSet<>();

        getField(fields, wrapper.getModel().getClass());

        for (Field field : fields) {
            // 排除主键
            if (field.getName().equals("fdId")) {
                continue;
            }
            Object value = ObjectUtil.readObjectValue(wrapper.getModel(), field.getName());
            if (wrapper.getUpdateType() == UpdateType.ALL) {
                update.set(field.getName(), value);
            } else if (wrapper.getUpdateType() == UpdateType.NOT_NULL) {
                if (!ObjectUtil.isNull(value)) {
                    update.set(field.getName(), value);
                }
            }
        }
    }

    /**
     * 获取类的所有字段（包含父类）
     */
    private static void getField(Set<Field> fields, Class<?> clazz) {
        if (clazz != null && clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            getField(fields, clazz.getSuperclass());
        }
    }
}
