package com.bingo.study.common.component.deprecatedInterface.aspect;

import com.bingo.study.common.component.deprecatedInterface.annotation.DeprecatedInterfaceSee;
import com.bingo.study.common.core.page.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;

/**
 * 接口移除提示实现
 *
 * @Author h-bingo
 * @Date 2023-04-23 16:00
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
@ConditionalOnMissingBean(DeprecatedInterfaceSeeAspect.class)
public class DeprecatedInterfaceSeeAspect implements InitializingBean {

    private static final String DEPRECATED_INTERFACE_TIP = "接口已失效，请使用：%s";

    @Pointcut("@annotation(com.bingo.study.common.component.deprecatedInterface.annotation.DeprecatedInterfaceSee)")
    public void interfaceSee() {
    }

    @Around(value = "interfaceSee()&&@annotation(deprecatedInterfaceSee)")
    public Object doAround(ProceedingJoinPoint joinPoint, DeprecatedInterfaceSee deprecatedInterfaceSee)
            throws Throwable {
        Object proceed = joinPoint.proceed();
        if (proceed instanceof AjaxResult) {
            AjaxResult ajaxResult = (AjaxResult) proceed;
            if (ajaxResult.checkIsSuccess()) {
                if (StringUtils.isNotBlank(deprecatedInterfaceSee.value())) {
                    ajaxResult.setMessage(String.format(DEPRECATED_INTERFACE_TIP, deprecatedInterfaceSee.value()));
                } else if (deprecatedInterfaceSee.feign() != DeprecatedInterfaceSee.class
                        && StringUtils.isNotBlank(deprecatedInterfaceSee.method())) {
                    Method[] methods = deprecatedInterfaceSee.feign().getMethods();
                    for (Method method : methods) {
                        if (method.getName().equalsIgnoreCase(deprecatedInterfaceSee.method())) {
                            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                            FeignClient feignClient = deprecatedInterfaceSee.feign().getAnnotation(FeignClient.class);
                            if (feignClient != null) {
                                ajaxResult.setMessage(String.format(DEPRECATED_INTERFACE_TIP,
                                        feignClient.path() + requestMapping.value()[0]));
                            } else {
                                ajaxResult.setMessage(String.format(DEPRECATED_INTERFACE_TIP,
                                        requestMapping.value()[0]));
                            }
                            return proceed;
                        }
                    }
                }
            }
        }
        return proceed;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("接口删除提示功能已开启，请在即将删除的接口添加: @DeprecatedInterfaceSee");
    }
}
