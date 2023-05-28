package com.bingo.study.common.transactional.holder;

import com.bingo.study.common.transactional.DynamicConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理当前嵌套事物连接
 *
 * @Author h-bingo
 * @Date 2023-05-11 17:34
 * @Version 1.0
 */
public class DynamicConnectionGroupHolder {

    private static final ThreadLocal<Map<String, List<DynamicConnection>>> GROUP_CONNECTION_LOCAL = new ThreadLocal<>();

    public static List<DynamicConnection> getGroupConnection(String groupId) {
        Map<String, List<DynamicConnection>> listMap = GROUP_CONNECTION_LOCAL.get();
        if (listMap == null) {
            return new ArrayList<>();
        }
        return listMap.get(groupId);
    }

    public static void setDynamicConnection(String groupId, DynamicConnection connection) {
        Map<String, List<DynamicConnection>> listMap = GROUP_CONNECTION_LOCAL.get();
        if (listMap == null) {
            listMap = new HashMap<>();
            GROUP_CONNECTION_LOCAL.set(listMap);
        }

        listMap.computeIfAbsent(groupId, k -> new ArrayList<>())
                .add(connection);
    }

    public static void clean() {
        GROUP_CONNECTION_LOCAL.remove();
    }
}
