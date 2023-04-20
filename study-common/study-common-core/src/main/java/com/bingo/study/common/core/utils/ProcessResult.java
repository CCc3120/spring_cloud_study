package com.bingo.study.common.core.utils;

import lombok.Getter;

/**
 * 业务处理结果返回，可包含成功返回的数据和失败返回的提示
 *
 * @param <T>
 */
public class ProcessResult<T> {

    private Boolean isSuccess;
    @Getter
    private T result;
    @Getter
    private String message;

    private ProcessResult(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    private ProcessResult(Boolean isSuccess, T result, String message) {
        this.isSuccess = isSuccess;
        this.result = result;
        this.message = message;
    }

    public Boolean isSuccess() {
        return isSuccess;
    }

    public static <T> ProcessResult<T> success() {
        return new ProcessResult<>(true);
    }

    public static <T> ProcessResult<T> success(T t) {
        return new ProcessResult<>(true, t, null);
    }

    public static <T> ProcessResult<T> fail() {
        return new ProcessResult<>(false);
    }

    public static <T> ProcessResult<T> fail(String message) {
        return new ProcessResult<>(false, null, message);
    }
}
