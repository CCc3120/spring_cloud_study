package com.bingo.test.proxy;


import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.cglib.beans.BeanMap;

import java.util.Map;

/**
 * @Author h-bingo
 * @Date 2023-08-11 17:27
 * @Version 1.0
 */
public class DynamicBean {

    private Object target;

    private BeanMap beanMap;

    public Object getTarget() {
        return target;
    }

    public Object getValue(String property) {
        return beanMap.get(property);
    }

    public void setValue(String property, Object value) {
        beanMap.put(property, value);
    }

    public DynamicBean(Class superClass, Map<String, Class> propertyMap) {
        this.target = generateBean(superClass, propertyMap);
        this.beanMap = BeanMap.create(target);
    }

    private Object generateBean(Class superClass, Map<String, Class> propertyMap) {
        BeanGenerator beanGenerator = new BeanGenerator();
        if (superClass != null) {
            beanGenerator.setSuperclass(superClass);
        }

        BeanGenerator.addProperties(beanGenerator, propertyMap);
        return beanGenerator.create();
    }
}
