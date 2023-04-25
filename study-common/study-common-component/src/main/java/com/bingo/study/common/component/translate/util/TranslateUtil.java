package com.bingo.study.common.component.translate.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReflectUtil;
import com.bingo.study.common.component.translate.annotation.Translate;
import com.bingo.study.common.component.translate.constant.TranslateType;
import com.bingo.study.common.component.translate.wrapper.TranslateFieldWrapper;
import com.bingo.study.common.core.enums.CodeDescEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 枚举或字典翻译
 *
 * @Author h-bingo
 * @Date 2023-04-24 14:13
 * @Version 1.0
 */
@Slf4j
public class TranslateUtil {

    private static final Map<Class<?>, List<TranslateFieldWrapper>> TRAN_FIELD_CACHE = new ConcurrentHashMap<>();

    /**
     * 翻译列表
     *
     * @Param [list]
     * @Return void
     * @Date 2023-04-24 16:06
     */
    public static <T> void translate(List<T> list) {
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        for (T t : list) {
            translate(t);
        }
    }

    /**
     * 翻译对象
     *
     * @Param [t]
     * @Return void
     * @Date 2023-04-24 16:05
     */
    public static <T> void translate(T t) {
        List<TranslateFieldWrapper> list = getObjectInfo(t);
        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        for (TranslateFieldWrapper fieldWrapper : list) {
            if (fieldWrapper.getTranslateType() == TranslateType.ENUM) {
                CodeDescEnum.enumTran(ReflectUtil.getFieldValue(t, fieldWrapper.getField()),
                        getEnumValues(fieldWrapper.getEnumClass()),
                        stringCodeDescEnum -> ReflectUtil.setFieldValue(t, fieldWrapper.getFullField(),
                                stringCodeDescEnum.getDesc()));
            } else if (fieldWrapper.getTranslateType() == TranslateType.DICT) {
                // TODO 结合字典完成，字典缓存

            }
        }
    }

    /**
     * 获取对象的翻译字段信息
     *
     * @Param [obj]
     * @Return java.util.List<com.bingo.study.common.component.translate.wrapper.TranslateFieldWrapper>
     * @Date 2023-04-24 16:05
     */
    private static List<TranslateFieldWrapper> getObjectInfo(Object obj) {
        if (obj == null) {
            return null;
        }

        if (TRAN_FIELD_CACHE.containsKey(obj.getClass())) {
            return TRAN_FIELD_CACHE.get(obj.getClass());
        }

        List<TranslateFieldWrapper> list = getTranslateField(obj.getClass());
        TRAN_FIELD_CACHE.put(obj.getClass(), list);
        return list;
    }

    /**
     * 获取所有翻译的字段，包含父类
     *
     * @Param [clazz]
     * @Return void
     * @Date 2023-04-24 14:35
     */
    private static List<TranslateFieldWrapper> getTranslateField(Class<?> clazz) {
        // 获取所有字段，包含父类，子类和父类存在名称相同的字段，则取子类的
        List<Field> list = getAllField(clazz);
        if (list.isEmpty()) {
            return null;
        }

        List<TranslateFieldWrapper> translateFieldList = new ArrayList<>();
        for (Field field : list) {
            Translate annotation = field.getAnnotation(Translate.class);
            if (annotation != null) {
                TranslateFieldWrapper translateField = new TranslateFieldWrapper();

                translateField.setTranslateType(annotation.type());
                translateField.setEnumClass(annotation.enumClass());
                translateField.setDictType(annotation.dictType());
                translateField.setField(field);

                if (StringUtils.isBlank(annotation.fillName())) {
                    translateField.setFullField(field);
                } else {
                    translateField.setFullField(ReflectUtil.getField(clazz, annotation.fillName()));
                }

                translateFieldList.add(translateField);
            }
        }
        return translateFieldList;
    }

    /**
     * 获取所有字段，包含父类，若子类和父类字段存在重名，则取子类的
     *
     * @Param [clazz]
     * @Return java.util.List<java.lang.reflect.Field>
     * @Date 2023-04-24 16:04
     */
    private static List<Field> getAllField(Class<?> clazz) {
        List<Field> list = new ArrayList<>();

        Set<String> fieldName = new HashSet<>();
        Class<?> aClass = clazz;
        while (aClass != null) {
            Field[] declaredFields = aClass.getDeclaredFields();
            for (Field field : declaredFields) {
                if (!fieldName.contains(field.getName())) {
                    fieldName.add(field.getName());
                    list.add(field);
                }
            }
            aClass = aClass.getSuperclass();
        }
        return list;
    }

    /**
     * 获取枚举的 values
     *
     * @Param [clazz]
     * @Return T[]
     * @Date 2023-04-24 16:04
     */
    private static <T extends CodeDescEnum<?>> T[] getEnumValues(Class<T> clazz) {
        if (!clazz.isEnum()) {
            log.info("{} 不是枚举类", clazz.getTypeName());
            return (T[]) new CodeDescEnum[0];
        }
        try {
            Method method = clazz.getDeclaredMethod("values");
            return (T[]) method.invoke(null);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            log.warn("{} 枚举值获取异常", clazz.getTypeName());
            return (T[]) new CodeDescEnum[0];
        }
    }
}
