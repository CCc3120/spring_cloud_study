package com.bingo.study.common.transactional.holder;

/**
 * @Author h-bingo
 * @Date 2023-05-11 17:11
 * @Version 1.0
 */
public class TransactionStatusHolder {

    /**
     * 当前请求是否开启多数据源事物
     */
    private static final ThreadLocal<Boolean> TRANSACTION_STATUS_LOCAL = new ThreadLocal<>();

    public static void begin() {
        TRANSACTION_STATUS_LOCAL.set(true);
    }

    public static void close() {
        TRANSACTION_STATUS_LOCAL.set(false);
    }

    public static Boolean isOpen() {
        return TRANSACTION_STATUS_LOCAL.get() != null && TRANSACTION_STATUS_LOCAL.get();
    }

    public static void clean() {
        TRANSACTION_STATUS_LOCAL.remove();
    }
}
