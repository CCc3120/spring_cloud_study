package com.bingo.study.common.core.page;

import org.springframework.util.Assert;

/**
 * @author bingo
 * @date 2022-03-24 13:45
 */
public class AjaxResultFactory {

    public static AjaxResult success() {
        return success(null);
    }

    public static AjaxResult success(Object data) {
        return new AjaxResultBuild(HttpStatusEnum.SUCCESS.getCode(), HttpStatusEnum.SUCCESS.getDesc(), data).result();
    }

    public static AjaxResult fail() {
        return fail(null);
    }

    public static AjaxResult fail(Object data) {
        return new AjaxResultBuild(HttpStatusEnum.FAIL.getCode(), HttpStatusEnum.FAIL.getDesc(), data).result();
    }

    public static AjaxResult error() {
        return error(null);
    }

    public static AjaxResult error(Object data) {
        return new AjaxResultBuild(HttpStatusEnum.ERROR.getCode(), HttpStatusEnum.ERROR.getDesc(), data).result();
    }

    public static AjaxResult build(HttpStatusEnum httpStatusEnum) {
        return build(httpStatusEnum, null);
    }

    public static AjaxResult build(HttpStatusEnum httpStatusEnum, Object data) {
        return new AjaxResultBuild(httpStatusEnum.getCode(), httpStatusEnum.getDesc(), data).result();
    }

    public static AjaxResultBuild build() {
        return new AjaxResultBuild();
    }

    public static class AjaxResultBuild {
        private String code;

        private String message;

        private Object data;

        protected AjaxResultBuild() {
        }

        protected AjaxResultBuild(String code, String message, Object data) {
            this.code = code;
            this.message = message;
            this.data = data;
        }

        public AjaxResultBuild code(String code) {
            this.code = code;
            return this;
        }

        public AjaxResultBuild message(String message) {
            this.message = message;
            return this;
        }

        public AjaxResultBuild data(Object data) {
            this.data = data;
            return this;
        }

        public AjaxResult result() {
            Assert.hasLength(this.code, "response status code is not null");
            return new AjaxResult(this.code, this.message, this.data);
        }
    }
}

