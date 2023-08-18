package com.bingo.dict.controller;

import com.bingo.dict.model.SysDictCategory;
import com.bingo.study.common.core.controller.BaseController;
import com.bingo.study.common.core.response.RSX;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author h-bingo
 * @Date 2023-08-17 17:26
 * @Version 1.0
 */
@SuppressWarnings("unused")
@ResponseBody
@ConditionalOnMissingBean(SysDictCategoryController.class)
public class SysDictCategoryController extends BaseController {


    public RSX<SysDictCategory> save(HttpServletRequest request, HttpServletResponse response,
            @RequestBody SysDictCategory category) {

        return success(category);
    }

    public RSX<String> update(HttpServletRequest request, HttpServletResponse response) {

        return success();
    }

    public RSX<String> delete(HttpServletRequest request, HttpServletResponse response) {

        return success();
    }
}
