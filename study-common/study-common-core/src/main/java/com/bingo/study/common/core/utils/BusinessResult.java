package com.bingo.study.common.core.utils;

import lombok.Getter;

/**
 * 业务处理结果返回，可包含成功返回的数据和失败返回的提示
 *
 * @param <T>
 */
public class BusinessResult<T> {

    private final Boolean isSuccess;
    @Getter
    private T result;
    @Getter
    private String message;

    private BusinessResult(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    private BusinessResult(Boolean isSuccess, T result, String message) {
        this.isSuccess = isSuccess;
        this.result = result;
        this.message = message;
    }

    public Boolean isSuccess() {
        return isSuccess;
    }

    public static <T> BusinessResult<T> success() {
        return new BusinessResult<>(true);
    }

    public static <T> BusinessResult<T> success(T t) {
        return new BusinessResult<>(true, t, null);
    }

    public static <T> BusinessResult<T> fail() {
        return new BusinessResult<>(false);
    }

    public static <T> BusinessResult<T> fail(String message) {
        return new BusinessResult<>(false, null, message);
    }
}
