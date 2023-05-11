package com.bingo.study.common.component.httpLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author h-bingo
 * @Date 2023-04-26 15:27
 * @Version 1.0
 */
@ConditionalOnMissingBean(HttpLogHandlerAutoConfig.class)
@Import({HttpLogHandlerInterceptor.class})
public class HttpLogHandlerAutoConfig implements WebMvcConfigurer {

    @Autowired
    private HttpLogHandlerInterceptor httpLogHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(httpLogHandlerInterceptor);
    }
}
