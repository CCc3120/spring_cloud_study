package com.bingo.study.common.core.web.page;

import org.springframework.util.Assert;

/**
 * @author bingo
 * @date 2022-03-24 13:45
 */
@Deprecated
public class AjaxResultFactory {

    public static <T> AjaxResult<T> success() {
        return build(HttpStatusEnum.SUCCESS);
    }

    public static <T> AjaxResult<T> success(T data) {
        return build(HttpStatusEnum.SUCCESS, null, data);
    }

    public static <T> AjaxResult<T> fail() {
        return build(HttpStatusEnum.FAIL);
    }

    public static <T> AjaxResult<T> fail(String errMsg) {
        return build(HttpStatusEnum.FAIL, errMsg);
    }

    public static <T> AjaxResult<T> error() {
        return build(HttpStatusEnum.ERROR);
    }

    public static <T> AjaxResult<T> error(String errMsg) {
        return build(HttpStatusEnum.ERROR, errMsg);
    }

    public static <T> AjaxResult<T> build(HttpStatusEnum httpStatusEnum) {
        return build(httpStatusEnum, null);
    }

    public static <T> AjaxResult<T> build(HttpStatusEnum httpStatusEnum, String errMsg) {
        return build(httpStatusEnum, errMsg, null);
    }

    public static <T> AjaxResult<T> build(HttpStatusEnum httpStatusEnum, String errMsg, T data) {
        return new AjaxResultBuild<>(httpStatusEnum.getCode(), httpStatusEnum.getDesc(), errMsg, data).build();
    }

    public static <T> AjaxResultBuild<T> builder() {
        return new AjaxResultBuild<>();
    }

    public static class AjaxResultBuild<T> {
        private String code;

        private String message;

        private String errMsg;

        private T data;

        protected AjaxResultBuild() {
        }

        protected AjaxResultBuild(String code, String message, String errMsg, T data) {
            this.code = code;
            this.message = message;
            this.errMsg = errMsg;
            this.data = data;
        }

        public AjaxResultBuild<T> code(String code) {
            this.code = code;
            return this;
        }

        public AjaxResultBuild<T> message(String message) {
            this.message = message;
            return this;
        }

        public AjaxResultBuild<T> errMsg(String errMsg) {
            this.errMsg = errMsg;
            return this;
        }

        public AjaxResultBuild<T> data(T data) {
            this.data = data;
            return this;
        }

        public AjaxResult<T> build() {
            Assert.hasLength(this.code, "response status code is not null");
            return new AjaxResult<>(this.code, this.message, this.errMsg, this.data);
        }
    }
}

