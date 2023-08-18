package com.bingo.test.exception;

import com.bingo.study.common.core.controller.BaseController;
import com.bingo.study.common.core.response.RSX;

/**
 * @Author h-bingo
 * @Date 2023-08-11 16:12
 * @Version 1.0
 */
// @RestControllerAdvice
public class ExceptionHandler extends BaseController {

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public RSX<Void> exception(Exception e) {

        return fail(e.getMessage());
    }
}
