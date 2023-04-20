package com.bingo.study.common.component.nosql.exception;

/**
 * @Author h-bingo
 * @Date 2023-04-20 10:31
 * @Version 1.0
 */
public class NoSqlException extends RuntimeException {

    public static final String NOT_ANNO = "%s Class not found @NoSql, please check it";

    public NoSqlException(Class<?> clazz) {
        super(String.format(NOT_ANNO, clazz.getTypeName()));
    }
}
