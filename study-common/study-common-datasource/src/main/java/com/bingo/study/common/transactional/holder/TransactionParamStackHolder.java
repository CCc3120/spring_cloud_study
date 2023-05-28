package com.bingo.study.common.transactional.holder;

import com.bingo.study.common.core.utils.stack.ArrayDequeStack;
import com.bingo.study.common.transactional.DynamicParamWrapper;

/**
 * 当前线程的所有事物栈
 *
 * @Author h-bingo
 * @Date 2023-05-11 17:16
 * @Version 1.0
 */
public class TransactionParamStackHolder {

    private static final ThreadLocal<ArrayDequeStack<DynamicParamWrapper>> TRANSACTION_PARAM_STACK_LOCAL =
            new ThreadLocal<>();

    public static void addTransactionParam(DynamicParamWrapper wrapper) {
        ArrayDequeStack<DynamicParamWrapper> stack = TRANSACTION_PARAM_STACK_LOCAL.get();
        if (stack == null) {
            stack = new ArrayDequeStack<>();
            TRANSACTION_PARAM_STACK_LOCAL.set(stack);
        }
        stack.push(wrapper);
    }

    public static DynamicParamWrapper getAndRemoveLatestTransactionParam() {
        ArrayDequeStack<DynamicParamWrapper> stack = TRANSACTION_PARAM_STACK_LOCAL.get();
        if (stack != null) {
            return stack.poll();
        }
        return null;
    }

    public static DynamicParamWrapper getLatestTransactionParam() {
        ArrayDequeStack<DynamicParamWrapper> stack = TRANSACTION_PARAM_STACK_LOCAL.get();
        if (stack != null) {
            return stack.peek();
        }
        return null;
    }

    public static void clean() {
        TRANSACTION_PARAM_STACK_LOCAL.remove();
    }
}
