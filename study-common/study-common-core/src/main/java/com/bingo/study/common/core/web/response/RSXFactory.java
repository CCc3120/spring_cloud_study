package com.bingo.study.common.core.web.response;

import org.springframework.util.Assert;

/**
 * @Author h-bingo
 * @Date 2023-08-18 15:59
 * @Version 1.0
 */
public class RSXFactory {

    public static <T> RSX<T> success() {
        return RSXFactory.<T>builder()
                .status(RSXStatusEnum.SUCCESS_STATUS.getCode())
                .message(RSXStatusEnum.SUCCESS_STATUS.getDesc())
                .build();
    }

    public static <T> RSX<T> success(T data) {
        return RSXFactory.<T>builder()
                .status(RSXStatusEnum.SUCCESS_STATUS.getCode())
                .message(RSXStatusEnum.SUCCESS_STATUS.getDesc())
                .data(data)
                .build();
    }

    public static <T> RSX<T> fail() {
        return RSXFactory.<T>builder()
                .status(RSXStatusEnum.FAIL_STATUS.getCode())
                .message(RSXStatusEnum.FAIL_STATUS.getDesc())
                .build();
    }

    // 手动失败原因
    public static <T> RSX<T> fail(String message) {
        return RSXFactory.<T>builder()
                .status(RSXStatusEnum.FAIL_STATUS.getCode())
                .message(message)
                .build();
    }

    // 失败报错
    public static <T> RSX<T> fail(String errCode, String errMsg, String module) {
        return RSXFactory.<T>builder()
                .status(RSXStatusEnum.FAIL_STATUS.getCode())
                .message(RSXStatusEnum.FAIL_STATUS.getDesc())
                .errCode(errCode)
                .errMsg(errMsg)
                .module(module)
                .build();
    }

    // 未知异常
    public static <T> RSX<T> error() {
        return RSXFactory.<T>builder()
                .status(RSXStatusEnum.ERROR_STATUS.getCode())
                .message(RSXStatusEnum.ERROR_STATUS.getDesc())
                .build();
    }

    // 失败报错
    public static <T> RSX<T> error(String errCode, String errMsg, String module) {
        return RSXFactory.<T>builder()
                .status(RSXStatusEnum.ERROR_STATUS.getCode())
                .message(RSXStatusEnum.ERROR_STATUS.getDesc())
                .errCode(errCode)
                .errMsg(errMsg)
                .module(module)
                .build();
    }

    public static <T> RSXBuild<T> builder() {
        return new RSXBuild<>();
    }

    public static class RSXBuild<T> {

        private String status;

        private String message;

        private T data;

        private String errCode;

        private String errMsg;

        private String module;

        protected RSXBuild() {
        }

        protected RSXBuild(String status, String message, T data, String errCode, String errMsg, String module) {
            this.status = status;
            this.message = message;
            this.data = data;
            this.errCode = errCode;
            this.errMsg = errMsg;
            this.module = module;
        }

        public RSXBuild<T> status(String status) {
            this.status = status;
            return this;
        }

        public RSXBuild<T> message(String message) {
            this.message = message;
            return this;
        }

        public RSXBuild<T> data(T data) {
            this.data = data;
            return this;
        }

        public RSXBuild<T> errCode(String errCode) {
            this.errCode = errCode;
            return this;
        }

        public RSXBuild<T> errMsg(String errMsg) {
            this.errMsg = errMsg;
            return this;
        }

        public RSXBuild<T> module(String module) {
            this.module = module;
            return this;
        }

        public RSX<T> build() {
            Assert.hasLength(this.status, "response status is not null");
            return new RSX<>(this.status, this.message, this.data, this.errCode, this.errMsg, this.module);
        }
    }
}
