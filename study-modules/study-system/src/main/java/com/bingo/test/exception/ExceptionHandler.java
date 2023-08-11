package com.bingo.test.exception;

import com.bingo.study.common.core.controller.BaseController;
import com.bingo.study.common.core.page.AjaxResult;

/**
 * @Author h-bingo
 * @Date 2023-08-11 16:12
 * @Version 1.0
 */
// @RestControllerAdvice
public class ExceptionHandler extends BaseController {

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public AjaxResult<String> exception(Exception e) {

        return error(e.getMessage());
    }
}
