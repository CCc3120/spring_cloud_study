package com.bingo.test.mainTest;

import com.bingo.study.common.component.lock.annotation.LockKey;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author h-bingo
 * @Date 2023-06-08 15:03
 * @Version 1.0
 */
public class TestMainClass {
    public static void main(String[] args) throws Exception {

        // AntPathMatcher matcher = new AntPathMatcher();
        // System.out.println(matcher.match("{uuid}", "xxxx"));
        // Map<String, String> result = matcher.extractUriTemplateVariables("{uuid}", "xxx");
        // System.out.println(result);

        // BeanInfo beanInfo = Introspector.getBeanInfo(TestMainClass.class);
        // MethodDescriptor[] methodDescriptors = beanInfo.getMethodDescriptors();
        // for (MethodDescriptor methodDescriptor : methodDescriptors) {
        //     System.out.println("method:" + methodDescriptor.getName());
        //     ParameterDescriptor[] params = methodDescriptor.getParameterDescriptors();
        //     if (params != null) {
        //         for (ParameterDescriptor param : params) {
        //             System.out.println("param:" + param.getName());
        //         }
        //     }
        // }
        //
        // Method[] methods = TestMainClass.class.getMethods();
        // for (Method method : methods) {
        //     if (method.getName().equals("test02")) {
        //         LocalVariableTableParameterNameDiscoverer discoverer =
        //                 new LocalVariableTableParameterNameDiscoverer();
        //         String[] methodNames = discoverer.getParameterNames(method);
        //         for (String methodName : methodNames) {
        //             System.out.println(methodName);
        //         }
        //     }
        // }

        // Student student = new Student();
        // student.setSex("nan");
        //
        // Map<String, Object> map = new HashMap<>();
        // map.put("sex", "zhangsan");
        // map.put("0", "zhangsan0");
        //
        // List<String> list = new ArrayList<>();
        // list.add("12312312");
        //
        String a = "xxxxx";
        //
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("map", a);
        Expression exp = parser.parseExpression("#map");
        System.out.println(exp.getValue(context, String.class));
    }

    public void test03() throws NoSuchMethodException {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        String stackId = "";

        TestMainClass testMainClass = new TestMainClass();

        testMainClass.test02(map, map2, stackId);

        Method method = testMainClass.getClass().getDeclaredMethod("test02", Map.class, Map.class, String.class);

        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {
            LockKey annotation = parameter.getAnnotation(LockKey.class);
            if (annotation != null) {
                System.out.println(parameter.getName() + "----" + parameter.getParameterizedType().getTypeName());
            }
        }

        LocalVariableTableParameterNameDiscoverer localVariableTable = new LocalVariableTableParameterNameDiscoverer();
        String[] paraNameArr = localVariableTable.getParameterNames(method);
        System.out.println(Arrays.toString(paraNameArr));
    }

    public void test02(@LockKey(alias = "stId") Map<String, Object> map, Map<String, Object> map2, String stackId) {

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
