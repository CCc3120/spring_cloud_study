package com.bingo.study.common.component.responseBodyHandle;

import cn.hutool.core.util.ArrayUtil;
import com.bingo.study.common.component.responseBodyHandle.annotation.ResponseBodyHandleMark;
import com.bingo.study.common.component.responseBodyHandle.handler.DefaultResponseBodyHandler;
import com.bingo.study.common.component.responseBodyHandle.handler.ResponseBodyHandler;
import com.bingo.study.common.core.utils.JsonMapper;
import com.bingo.study.common.core.web.response.RSX;
import com.bingo.study.common.core.web.response.RSXFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 默认统一返回值处理
 *
 * @Author h-bingo
 * @Date 2023-04-23 09:35
 * @Version 1.0
 */
@Slf4j
@ControllerAdvice
@ConditionalOnMissingBean(DefaultResponseBodyWrapperFilter.class)
public class DefaultResponseBodyWrapperFilter implements ResponseBodyAdvice<Object>, InitializingBean {

    @Autowired
    private DefaultResponseBodyHandler defaultResponseBodyHandler;

    @Autowired
    private List<ResponseBodyHandler> responseBodyHandlerList;

    private final String[] defaultIgnore = IgnoreField.fields();


    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
            ServerHttpResponse response) {

        ResponseBodyHandleMark responseBodyHandleMark = getMethodAnnotation(returnType.getMethod(),
                ResponseBodyHandleMark.class);

        if (body instanceof RSX) {
            RSX rsx = (RSX) body;
            rsx.setData(process(rsx.getData(), responseBodyHandleMark));
            return rsx;
        } else {
            Object data = process(body, responseBodyHandleMark);
            if (body instanceof String) {
                if (responseBodyHandleMark == null || responseBodyHandleMark.wrapper()) {
                    return JsonMapper.getInstance().toJsonString(RSXFactory.success(data));
                }
            } else {
                if (responseBodyHandleMark == null || responseBodyHandleMark.wrapper()) {
                    return RSXFactory.success(data);
                }
            }
            return data;
        }
    }

    private Object process(Object data, ResponseBodyHandleMark responseBodyHandleMark) {
        if (data == null) {
            return null;
        } else if (responseBodyHandleMark == null) {
            return processIgnore(data, defaultIgnore);
        } else if (!responseBodyHandleMark.filedFilter()) {
            return data;
        } else if (responseBodyHandleMark.specify().length > 0) {
            return processSpecify(data, responseBodyHandleMark.specify());
        } else {
            return processIgnore(data, responseBodyHandleMark.ignore());
        }
    }

    private Object processSpecify(Object obj, String[] specify) {
        if (ArrayUtil.isEmpty(specify)) {
            return obj;
        }

        for (ResponseBodyHandler responseBodyHandler : responseBodyHandlerList) {
            if (responseBodyHandler.support(obj)) {
                return responseBodyHandler.specify(obj, specify);
            }
        }
        return defaultResponseBodyHandler.specify(obj, specify);
    }

    private Object processIgnore(Object obj, String[] ignore) {
        if (ArrayUtil.isEmpty(ignore)) {
            ignore = defaultIgnore;
        }

        for (ResponseBodyHandler responseBodyHandler : responseBodyHandlerList) {
            if (responseBodyHandler.support(obj)) {
                return responseBodyHandler.ignore(obj, ignore);
            }
        }
        return defaultResponseBodyHandler.ignore(obj, ignore);
    }

    private <T extends Annotation> T getMethodAnnotation(Method method, Class<T> clazz) {
        T annotation = AnnotationUtils.findAnnotation(method, clazz);
        if (annotation == null) {
            annotation = AnnotationUtils.findAnnotation(method.getDeclaringClass(), clazz);
        }
        return annotation;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("系统全局返回值包装、处理功能已开启，如需忽略功能请在接口添加: @ResponseBodyHandleMark(filedFilter = false, wrapper = false)");
    }
}


