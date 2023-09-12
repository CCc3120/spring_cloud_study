package com.bingo.test.mainTest;

import com.bingo.study.common.core.utils.ObjectUtil;
import com.bingo.test.translate.dict.DictCommon;
import com.bingo.test.translate.dict.DictType;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Author h-bingo
 * @Date 2023-06-08 15:03
 * @Version 1.0
 */
public class TestMainClass {
    public static void main(String[] args) {
        // Class<DictType> objectClass = test01(DictCommon.class);

        Class<Object> objectClass1 = ObjectUtil.paradigmParameterizedType(DictCommon.class);
        Type[] classes = ObjectUtil.paradigmParameterizedTypes(DictCommon.class);
        System.out.println();
    }


    static <T> Class<T> test01(Class<?> clazz) {
        Type type = clazz.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            System.out.println(parameterizedType.getActualTypeArguments()[0]);
            return (Class<T>) parameterizedType.getActualTypeArguments()[0];
        }
        throw new RuntimeException();
    }
}
