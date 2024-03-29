package com.bingo.study.common.core.utils;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author h-bingo
 * @Date 2023-05-15 09:46
 * @Version 1.0
 */
public class AspectUtil {

    /**
     * 获取完整的方法名
     * <p>
     * 例如本方法：com.bingo.study.common.core.utils.AspectUtil#getMethodIntactName(org.aspectj.lang.ProceedingJoinPoint)
     *
     * @Param [joinPoint]
     * @Return java.lang.String
     * @Date 2023-04-25 14:13
     */
    public static String getMethodIntactName(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getTypeName();
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        String methodName = ms.getName();
        Class<?>[] parameterTypes = ms.getParameterTypes();
        List<String> collect = Arrays.stream(parameterTypes).map(Class::getSimpleName).collect(Collectors.toList());

        StringBuilder res = new StringBuilder(className);
        res.append(".").append(methodName).append("(");
        if (!CollectionUtil.isEmpty(collect)) {
            res.append(StringUtils.join(collect, ","));
        }
        res.append(")");
        return res.toString();
    }

    /***
     * 获取方法上的注解，如果没有则找方法类上的注解
     * @Param [method, clazz]
     * @Return T
     * @Date 2023-09-14 14:49
     */
    public static <T extends Annotation> T getMethodAnnotation(Method method, Class<T> clazz) {
        T annotation = AnnotationUtils.findAnnotation(method, clazz);
        if (annotation == null) {
            annotation = AnnotationUtils.findAnnotation(method.getDeclaringClass(), clazz);
        }
        return annotation;
    }
}
