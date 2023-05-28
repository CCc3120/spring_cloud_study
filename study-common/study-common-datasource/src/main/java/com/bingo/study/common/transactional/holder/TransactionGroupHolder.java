package com.bingo.study.common.transactional.holder;

import com.bingo.study.common.core.utils.stack.ArrayDequeStack;
import com.bingo.study.common.transactional.DynamicParamWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 当前事物分组
 *
 * @Author h-bingo
 * @Date 2023-05-11 17:23
 * @Version 1.0
 */
public class TransactionGroupHolder {

    /**
     * 嵌套事物组合
     * <p>
     * 事物id - 事物参数列表
     */
    private static final ThreadLocal<Map<String, List<DynamicParamWrapper>>> GROUP_PARAM_LOCAL = new ThreadLocal<>();

    /**
     * 嵌套事物分组ID
     */
    private static final ThreadLocal<ArrayDequeStack<String>> GROUP_ID_LOCAL = new ThreadLocal<>();

    public static void addGroupId(String groupId) {
        ArrayDequeStack<String> dequeStack = GROUP_ID_LOCAL.get();
        if (dequeStack == null) {
            dequeStack = new ArrayDequeStack<>();
            GROUP_ID_LOCAL.set(dequeStack);
        }

        if (groupId != null && !groupId.equals(dequeStack.peek())) {
            dequeStack.push(groupId);
        }
    }

    public static void addTransaction(String groupId, DynamicParamWrapper wrapper) {
        addGroupId(groupId);

        String groupId_ = GROUP_ID_LOCAL.get().peek();

        Map<String, List<DynamicParamWrapper>> map = GROUP_PARAM_LOCAL.get();
        if (map == null) {
            map = new HashMap<>();
            GROUP_PARAM_LOCAL.set(map);
        }

        map.computeIfAbsent(groupId_, k -> new ArrayList<>())
                .add(wrapper);
    }

    public static List<DynamicParamWrapper> getGroupParam(String groupId) {
        Map<String, List<DynamicParamWrapper>> map = GROUP_PARAM_LOCAL.get();
        if (map == null) {
            return null;
        }

        return map.get(groupId);
    }

    public static String getLatestGroupId() {
        ArrayDequeStack<String> dequeStack = GROUP_ID_LOCAL.get();
        if (dequeStack == null) {
            return null;
        }

        return GROUP_ID_LOCAL.get().peek();
    }

    public static String getAndRemoveLatestGroupId() {
        ArrayDequeStack<String> dequeStack = GROUP_ID_LOCAL.get();
        if (dequeStack == null) {
            return null;
        }

        return GROUP_ID_LOCAL.get().poll();
    }

    public static void clean() {
        GROUP_PARAM_LOCAL.remove();
        GROUP_ID_LOCAL.remove();
    }
}
