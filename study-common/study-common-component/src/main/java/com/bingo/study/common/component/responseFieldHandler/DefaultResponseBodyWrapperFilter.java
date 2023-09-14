package com.bingo.study.common.component.responseFieldHandler;

import cn.hutool.core.util.ArrayUtil;
import com.bingo.study.common.component.responseFieldHandler.annotation.ResponseField;
import com.bingo.study.common.component.responseFieldHandler.handler.DefaultResponseBodyHandler;
import com.bingo.study.common.component.responseFieldHandler.handler.ResponseBodyHandler;
import com.bingo.study.common.core.utils.AspectUtil;
import com.bingo.study.common.core.utils.JsonMapper;
import com.bingo.study.common.core.web.response.RSX;
import com.bingo.study.common.core.web.response.RSXFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

/**
 * 默认统一返回值处理
 *
 * @Author h-bingo
 * @Date 2023-04-23 09:35
 * @Version 1.0
 */
@Slf4j
@SuppressWarnings({"rawtypes", "unchecked"})
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

        ResponseField responseField = AspectUtil.getMethodAnnotation(returnType.getMethod(),
                ResponseField.class);

        if (body instanceof RSX) {
            RSX rsx = (RSX) body;
            rsx.setData(this.process(rsx.getData(), responseField));
            return rsx;
        } else {
            Object data = this.process(body, responseField);
            if (body instanceof String) {
                if (responseField == null || responseField.wrapper()) {
                    return JsonMapper.getInstance().toJsonString(RSXFactory.success(data));
                }
            } else {
                if (responseField == null || responseField.wrapper()) {
                    return RSXFactory.success(data);
                }
            }
            return data;
        }
    }

    private Object process(Object data, ResponseField responseField) {
        if (data == null) {
            return null;
        } else if (responseField == null) {
            return this.processIgnore(data, defaultIgnore);
        } else if (!responseField.filedFilter()) {
            return data;
        } else if (responseField.specify().length > 0) {
            return this.processSpecify(data, responseField.specify());
        } else {
            return this.processIgnore(data, responseField.ignore());
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

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("系统全局返回值包装、处理功能已开启，如需忽略功能请在接口添加: @ResponseBodyHandleMark(filedFilter = false, wrapper = false)");
    }
}


