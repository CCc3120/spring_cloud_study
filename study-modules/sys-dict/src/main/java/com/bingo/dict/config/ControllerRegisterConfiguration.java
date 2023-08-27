package com.bingo.dict.config;

import com.bingo.dict.controller.SysDictCategoryController;
import com.bingo.dict.controller.SysDictDataController;
import com.bingo.dict.enums.MappingRegisterEnum;
import com.bingo.study.common.core.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Import;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * 控制器注册
 *
 * @Author h-bingo
 * @Date 2023-08-17 15:26
 * @Version 1.0
 */
@Slf4j
// @Configuration
// @Import({TestController.class})
@Import({SysDictDataController.class, SysDictCategoryController.class})
@ConditionalOnMissingBean(ControllerRegisterConfiguration.class)
public class ControllerRegisterConfiguration {

    @Autowired
    public void setRequestMapping(RequestMappingHandlerMapping mapping) {
        MappingRegisterEnum[] registerEnums = MappingRegisterEnum.values();
        for (MappingRegisterEnum registerEnum : registerEnums) {
            registerMapping(mapping, registerEnum);
        }
        log.info("registerMapping complete...");
    }

    private void registerMapping(RequestMappingHandlerMapping mapping, MappingRegisterEnum registerEnum) {
        // 找到处理该路由的方法
        Method targetMethod = ReflectionUtils.findMethod(registerEnum.getBeanClass(), registerEnum.getMethodName(),
                registerEnum.getParamClass());

        PatternsRequestCondition patternsRequestCondition = new PatternsRequestCondition(registerEnum.getPath());
        RequestMethodsRequestCondition requestMethodsRequestCondition =
                new RequestMethodsRequestCondition(registerEnum.getRequestMethod());
        // 构造RequestMappingInfo对象
        RequestMappingInfo requestMappingInfo = new RequestMappingInfo(patternsRequestCondition,
                requestMethodsRequestCondition, null, null, null, null, null);
        // 注册映射处理
        mapping.registerMapping(requestMappingInfo, SpringUtil.getBean(registerEnum.getBeanClass()), targetMethod);
    }
}
