package com.bingo.study.common.component.returnValue.handler;

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
@ConditionalOnMissingBean(ListValueHandler.class)
public class ListValueHandler implements ReturnValueHandler {
    @Override
    public boolean support(Object obj) {
        return obj instanceof List;
    }

    @Override
    public Object ignore(Object obj, String[] ignore) {
        List list = (List) obj;
        if (CollectionUtil.isEmpty(list)) {
            return list;
        }

        List<Object> rtn = new ArrayList<>();
        for (Object o : list) {
            String string = JsonMapper.getInstance().toJsonString(o);
            Map map = JsonMapper.getInstance().fromJson(string, Map.class);
            for (String s : ignore) {
                map.remove(s);
            }
            rtn.add(map);
        }
        return rtn;
    }

    @Override
    public Object specify(Object obj, String[] specify) {
        List list = (List) obj;
        if (CollectionUtil.isEmpty(list)) {
            return list;
        }

        List<Object> rtn = new ArrayList<>();
        for (Object o : list) {
            String string = JsonMapper.getInstance().toJsonString(o);
            Map map = JsonMapper.getInstance().fromJson(string, Map.class);
            Map rtnMap = new HashMap();
            for (String s : specify) {
                rtnMap.put(s, map.get(s));
            }
            rtn.add(rtnMap);
        }
        return rtn;
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE - 1;
    }
}
