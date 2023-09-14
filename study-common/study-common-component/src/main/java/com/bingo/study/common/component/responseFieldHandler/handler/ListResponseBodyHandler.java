package com.bingo.study.common.component.responseFieldHandler.handler;

import cn.hutool.core.collection.CollectionUtil;
import com.bingo.study.common.core.utils.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author h-bingo
 * @Date 2023-04-23 09:48
 * @Version 1.0
 */
@Slf4j
@ConditionalOnMissingBean(ListResponseBodyHandler.class)
public class ListResponseBodyHandler implements ResponseBodyHandler {
    @Override
    public boolean support(Object obj) {
        return obj instanceof List;
    }

    @Override
    public Object ignore(Object obj, String[] ignore) {
        return this.process(obj, ignore, true);
    }

    @Override
    public Object specify(Object obj, String[] specify) {
        return this.process(obj, specify, false);
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE - 1;
    }

    private Object process(Object obj, String[] fields, boolean isIgnore) {
        List<?> list = (List<?>) obj;
        if (CollectionUtil.isEmpty(list)) {
            return list;
        }

        List<Object> rtn = new ArrayList<>();
        for (Object o : list) {
            String string = JsonMapper.getInstance().toJsonString(o);
            Map<?, ?> map = JsonMapper.getInstance().fromJson(string, Map.class);
            if (isIgnore) {
                for (String s : fields) {
                    map.remove(s);
                }
                rtn.add(map);
            } else {
                Map<String, Object> rtnMap = new HashMap<>();
                for (String s : fields) {
                    rtnMap.put(s, map.get(s));
                }
                rtn.add(rtnMap);
            }
        }
        return rtn;
    }
}
