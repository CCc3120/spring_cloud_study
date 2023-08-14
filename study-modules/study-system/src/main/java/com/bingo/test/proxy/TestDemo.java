package com.bingo.test.proxy;

import com.bingo.study.common.core.utils.JsonMapper;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author h-bingo
 * @Date 2023-08-11 17:26
 * @Version 1.0
 */
public class TestDemo {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        User user = new User();
        user.setFdId("123");
        user.setFdName("ssss");

        Class<? extends User> aClass = user.getClass();

        Class<?> forredName = Class.forName("com.bingo.test.translate.dict.DictCommon");
        Class<?>[] interfaces = forredName.getInterfaces();
        Class<?> superclass = forredName.getSuperclass();
        // Field fdAge = aClass.getDeclaredField("fdAge");
        // fdAge.setAccessible(true);
        // fdAge.set(user, "22");

        // System.out.println(fdAge.get(user));
        System.out.println(JsonMapper.getInstance().toJsonString(user));

    }

    public static void test01() throws InvocationTargetException, IllegalAccessException {
        User user = new User();
        user.setFdId("123");
        user.setFdName("ssss");

        System.out.println(JsonMapper.getInstance().toJsonString(user));

        Map<String, Object> map = new HashMap<>();
        map.put("age", 22);

        Object object = ReflectUtil.getObject(user, map);
        System.out.println(JsonMapper.getInstance().toJsonString(object));
    }
}
