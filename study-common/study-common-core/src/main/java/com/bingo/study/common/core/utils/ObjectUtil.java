package com.bingo.study.common.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;

/**
 * @author bingo
 * @date 2022-03-23 18:04
 */
@Slf4j
public class ObjectUtil {

    /**
     * 是否为空
     *
     * @param obj
     * @return
     */
    public static boolean isNull(Object obj) {
        return equals(obj, null);
    }

    /**
     * 比较两个对象是否相等（同时处理null的情况），当两个对象都为空时返回true
     *
     * @param obj1
     * @param obj2
     * @return
     */
    public static boolean equals(Object obj1, Object obj2) {
        return equals(obj1, obj2, true);
    }

    /**
     * 比较两个对象是否相等（同时处理null的情况），当两个对象都为空时返回指定值bothNullReturn
     *
     * @param obj1
     * @param obj2
     * @param bothNullReturn
     * @return
     */
    public static boolean equals(Object obj1, Object obj2, boolean bothNullReturn) {
        if (obj1 == null && obj2 == null) {
            return bothNullReturn;
        }
        if (obj1 == obj2) {
            return true;
        }
        if (obj1 == null || obj2 == null) {
            return false;
        }
        return obj1.equals(obj2);
    }

    /**
     * 将一个对象转换为Long类型
     *
     * @param value
     * @return
     */
    public static Long parseLong(Object value) {
        if (value instanceof Long) {
            return (Long) value;
        }
        if (value instanceof String) {
            return new Long((String) value);
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return null;
    }

    /**
     * 将一个对象转换为Double类型
     *
     * @param value
     * @return
     */
    public static Double parseDouble(Object value) {
        if (value != null) {
            try {
                if (value instanceof Double) {
                    return (Double) value;
                }
                if (value instanceof String) {
                    return new Double((String) value);
                }
                if (value instanceof Number) {
                    return ((Number) value).doubleValue();
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 读对象属性
     *
     * @param obj
     * @param fieldName
     * @return
     * @throws Exception
     */
    public static Object readObjectValue(Object obj, String fieldName) {
        Object value = null;
        try {
            PropertyDescriptor descriptor = new PropertyDescriptor(fieldName, obj.getClass());
            Method method = descriptor.getReadMethod();
            value = method.invoke(obj);
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            log.warn("read object value fail", e);
        }
        return value;
    }

    /**
     * 写对象属性
     *
     * @param obj
     * @param fieldName
     * @param value
     * @throws Exception
     */
    public static void writeObjectValue(Object obj, String fieldName, Object value) {
        try {
            PropertyDescriptor descriptor = new PropertyDescriptor(fieldName, obj.getClass());
            Method method = descriptor.getWriteMethod();
            method.invoke(obj, value);
        } catch (Exception e) {
            log.warn("write object value fail", e);
        }
    }

    /**
     * 获取 目标对象
     *
     * @param proxy 代理对象
     * @return
     * @throws Exception
     */
    public static Object getTarget(Object proxy) throws Exception {
        if (!AopUtils.isAopProxy(proxy)) {
            return proxy;// 不是代理对象
        }
        if (AopUtils.isJdkDynamicProxy(proxy)) {
            return getJdkDynamicProxyTargetObject(proxy);
        } else { // cglib
            return getCglibProxyTargetObject(proxy);
        }
    }

    /**
     * 获取类的范型参数，如果没有则返回 null
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Class<T> paradigmParameterizedType(Class<?> clazz) {
        Type[] types = paradigmParameterizedTypes(clazz);
        if (types == null) {
            return null;
        }
        return (Class<T>) types[0];
    }

    /**
     * 获取类的范型参数（数组），如果没有则返回 null
     *
     * @param clazz
     * @return
     */
    public static Type[] paradigmParameterizedTypes(Class<?> clazz) {
        Type type = clazz.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return parameterizedType.getActualTypeArguments();
        }
        log.info("{} 没有范型参数", clazz.getTypeName());
        return null;
    }

    public static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field field = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        field.setAccessible(true);
        Object dynamicAdvisedInterceptor = field.get(proxy);
        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        return ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
    }


    public static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field field = proxy.getClass().getSuperclass().getDeclaredField("h");
        field.setAccessible(true);
        AopProxy aopProxy = (AopProxy) field.get(proxy);
        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        return ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
    }
}
