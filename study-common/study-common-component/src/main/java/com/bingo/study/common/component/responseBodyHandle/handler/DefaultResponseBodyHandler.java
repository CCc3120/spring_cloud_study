package com.bingo.study.common.component.responseBodyHandle.handler;

import com.bingo.study.common.core.utils.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author h-bingo
 * @Date 2023-04-23 09:35
 * @Version 1.0
 */
@Slf4j
@ConditionalOnMissingBean(DefaultResponseBodyHandler.class)
public class DefaultResponseBodyHandler implements ResponseBodyHandler {
    @Override
    public boolean support(Object obj) {
        return false;
    }

    @Override
    public Object ignore(Object obj, String[] ignore) {
        String string = JsonMapper.getInstance().toJsonString(obj);
        Map<?, ?> map = JsonMapper.getInstance().fromJson(string, Map.class);
        for (String s : ignore) {
            map.remove(s);
        }
        return map;
    }

    @Override
    public Object specify(Object obj, String[] specify) {
        Map<Object, Object> rtn = new HashMap<>();
        String string = JsonMapper.getInstance().toJsonString(obj);
        Map<?, ?> map = JsonMapper.getInstance().fromJson(string, Map.class);
        for (String s : specify) {
            rtn.put(s, map.get(s));
        }
        return rtn;
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
