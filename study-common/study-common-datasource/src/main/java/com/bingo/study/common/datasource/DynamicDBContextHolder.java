package com.bingo.study.common.datasource;

/**
 * @Author h-bingo
 * @Date 2023-04-25 17:24
 * @Version 1.0
 */
public class DynamicDBContextHolder {

    private static final ThreadLocal<String> DATA_SOURCE = new ThreadLocal<>();

    public static void setDataSource(String dataSource) {
        DATA_SOURCE.set(dataSource);
    }

    public static String getDataSource() {
        return DATA_SOURCE.get();
    }

    public static void cleanDataSource() {
        DATA_SOURCE.remove();
    }
}
