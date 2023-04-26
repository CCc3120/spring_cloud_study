package com.bingo.study.common.component.returnValue;

import cn.hutool.core.util.ArrayUtil;
import com.bingo.study.common.component.returnValue.annotation.ReturnField;
import com.bingo.study.common.component.returnValue.handler.DefaultValueHandler;
import com.bingo.study.common.component.returnValue.handler.ReturnValueHandler;
import com.bingo.study.common.core.page.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 返回值拦截处理
 *
 * @Author h-bingo
 * @Date 2023-04-23 09:35
 * @Version 1.0
 */
@Slf4j
@ControllerAdvice
@ConditionalOnMissingBean(ReturnValueAdvice.class)
public class ReturnValueAdvice implements ResponseBodyAdvice<AjaxResult>, InitializingBean {

    @Autowired
    private DefaultValueHandler defaultValueHandler;

    @Autowired
    private List<ReturnValueHandler> returnValueHandlerList;

    private final String[] defaultIgnore = IgnoreField.fields();


    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 非AjaxResult类型不处理
        return returnType.getParameterType() == AjaxResult.class;
    }

    @Override
    public AjaxResult beforeBodyWrite(AjaxResult body, MethodParameter returnType,
            MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request, ServerHttpResponse response) {

        if (body == null) {
            return null;
        }

        // 状态码异常直接返回
        if (response instanceof ServletServerHttpResponse) {
            HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
            if (servletResponse.getStatus() != HttpStatus.OK.value()) {
                return body;
            }
        }

        // 非json不处理
        if (!selectedContentType.equals(MediaType.APPLICATION_JSON)
                && !(selectedContentType.equals(MediaType.APPLICATION_JSON_UTF8))) {
            return body;
        }

        // 失败不处理
        if (!body.checkIsSuccess() || body.getData() == null) {
            return body;
        }

        return process(body, returnType);
    }


    private AjaxResult process(AjaxResult ajaxResult, MethodParameter returnType) {
        ReturnField returnField = getReturnFieldAnno(returnType.getMethod());

        if (returnField == null) {
            ajaxResult.setData(processIgnore(ajaxResult.getData(), defaultIgnore));
        } else if (!returnField.enable()) {
            return ajaxResult;
        } else if (returnField.specify().length > 0) {
            ajaxResult.setData(processSpecify(ajaxResult.getData(), returnField.specify()));
        } else {
            ajaxResult.setData(processIgnore(ajaxResult.getData(), returnField.ignore()));
        }
        return ajaxResult;
    }

    private Object processSpecify(Object obj, String[] specify) {
        if (ArrayUtil.isEmpty(specify)) {
            return obj;
        }

        for (ReturnValueHandler returnValueHandler : returnValueHandlerList) {
            if (returnValueHandler.support(obj)) {
                return returnValueHandler.specify(obj, specify);
            }
        }
        return defaultValueHandler.specify(obj, specify);
    }

    private Object processIgnore(Object obj, String[] ignore) {
        if (ArrayUtil.isEmpty(ignore)) {
            ignore = defaultIgnore;
        }

        for (ReturnValueHandler returnValueHandler : returnValueHandlerList) {
            if (returnValueHandler.support(obj)) {
                return returnValueHandler.ignore(obj, ignore);
            }
        }
        return defaultValueHandler.ignore(obj, ignore);
    }

    private ReturnField getReturnFieldAnno(Method method) {
        ReturnField annotation = AnnotationUtils.findAnnotation(method, ReturnField.class);
        if (annotation == null) {
            annotation = AnnotationUtils.findAnnotation(method.getDeclaringClass(), ReturnField.class);
        }
        return annotation;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("系统全局返回值处理功能已开启，如需忽略功能请在接口添加: @ReturnField(enable = false)");
    }
}
