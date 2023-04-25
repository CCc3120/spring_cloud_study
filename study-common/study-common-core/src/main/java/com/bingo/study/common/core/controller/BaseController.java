package com.bingo.study.common.core.controller;


import com.bingo.study.common.core.page.AjaxResult;
import com.bingo.study.common.core.page.AjaxResultFactory;

/**
 * @author bingo
 * @date 2022-04-08 16:38
 */
public abstract class BaseController {

    protected <T> AjaxResult<T> success() {
        return AjaxResultFactory.success();
    }

    protected <T> AjaxResult<T> success(T data) {
        return AjaxResultFactory.success(data);
    }

    protected <T> AjaxResult<T> fail() {
        return AjaxResultFactory.fail();
    }

    protected <T> AjaxResult<T> fail(String errMsg) {
        return AjaxResultFactory.fail(errMsg);
    }

    protected <T> AjaxResult<T> error() {
        return AjaxResultFactory.error();
    }

    protected <T> AjaxResult<T> error(String errMsg) {
        return AjaxResultFactory.error(errMsg);
    }
}
