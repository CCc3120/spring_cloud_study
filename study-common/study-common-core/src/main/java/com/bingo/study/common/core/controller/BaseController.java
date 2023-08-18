package com.bingo.study.common.core.controller;


import com.bingo.study.common.core.response.RSX;
import com.bingo.study.common.core.response.RSXFactory;

/**
 * @author bingo
 * @date 2022-04-08 16:38
 */
public abstract class BaseController {

    // protected <T> AjaxResult<T> success() {
    //     return AjaxResultFactory.success();
    // }
    //
    // protected <T> AjaxResult<T> success(T data) {
    //     return AjaxResultFactory.success(data);
    // }
    //
    // protected <T> AjaxResult<T> fail() {
    //     return AjaxResultFactory.fail();
    // }
    //
    // protected <T> AjaxResult<T> fail(String errMsg) {
    //     return AjaxResultFactory.fail(errMsg);
    // }
    //
    // protected <T> AjaxResult<T> error() {
    //     return AjaxResultFactory.error();
    // }
    //
    // protected <T> AjaxResult<T> error(String errMsg) {
    //     return AjaxResultFactory.error(errMsg);
    // } RXS

    protected <T> RSX<T> success() {
        return RSXFactory.success();
    }

    protected <T> RSX<T> success(T data) {
        return RSXFactory.success(data);
    }

    protected <T> RSX<T> fail() {
        return RSXFactory.fail();
    }

    protected <T> RSX<T> fail(String message) {
        return RSXFactory.fail(message);
    }

    protected <T> RSX<T> fail(String errCode, String errMsg, String module) {
        return RSXFactory.fail(errCode, errMsg, module);
    }

    protected <T> RSX<T> error() {
        return RSXFactory.error();
    }

    protected <T> RSX<T> error(String errCode, String errMsg, String module) {
        return RSXFactory.error(errCode, errMsg, module);
    }
}
