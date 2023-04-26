package com.bingo.study.common.component.httpLog;

import cn.hutool.core.date.SystemClock;
import com.bingo.study.common.core.utils.IPUtil;
import com.bingo.study.common.core.utils.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author h-bingo
 * @Date 2023-04-26 14:47
 * @Version 1.0
 */
@Component
@ConditionalOnMissingBean(HttpLogHandlerInterceptor.class)
public class HttpLogHandlerInterceptor implements HandlerInterceptor, InitializingBean {

    private static final Logger log = LoggerFactory.getLogger("[ === Request LOG === ]");

    private static NamedThreadLocal<Long> COST_TIME = new NamedThreadLocal<>("HttpLogHandlerInterceptor");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        COST_TIME.set(SystemClock.now());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) throws Exception {
        try {
            Map<Object, Object> paramMap = new HashMap<>();
            String method = request.getMethod();
            String servletPath = request.getServletPath();
            String ipAddr = IPUtil.getIpAddr(request);
            // if (HttpMethod.PUT.name().equals(method) || HttpMethod.POST.name().equals(method)) {
            //     String body = argsArrayToString()
            // }

            if (ex != null) {
                paramMap.put("errorType", ex.getClass().getTypeName());
                paramMap.put("errorMsg", ex.getMessage());
                paramMap.put("errorStack", getExceptionStack(ex));
            }

            long costTime = SystemClock.now() - COST_TIME.get();

            paramMap.put("method", method);
            paramMap.put("servletPath", servletPath);
            paramMap.put("ip", ipAddr);
            paramMap.put("costTime", costTime + "ms");
            paramMap.put("params", request.getParameterMap());
            log.info(JsonMapper.getInstance().toJsonString(paramMap));
        } catch (Exception ignored) {

        } finally {
            COST_TIME.remove();
        }
    }

    private String argsArrayToString(Object[] args) {
        StringBuilder paramStr = new StringBuilder();
        if (args != null) {
            for (Object arg : args) {
                if (arg != null && !isFilter(arg)) {
                    paramStr.append(JsonMapper.getInstance().toJsonString(arg)).append(" ");
                }
            }
        }
        return paramStr.toString().trim();
    }

    private boolean isFilter(Object obj) {
        Class<?> aClass = obj.getClass();
        if (aClass.isArray()) {
            return aClass.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(aClass)) {
            Collection<?> collection = (Collection<?>) obj;
            for (Object o : collection) {
                return o instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(aClass)) {
            Map<?, ?> map = (Map<?, ?>) obj;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return obj instanceof MultipartFile
                || obj instanceof HttpServletRequest
                || obj instanceof HttpServletResponse
                || obj instanceof BindingResult;
    }

    private String getExceptionStack(Throwable e) {
        if (e == null) {
            return null;
        }
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("请求日志打印已开启");
    }
}
