package com.bingo.dict.controller;

import com.bingo.dict.model.SysDictData;
import com.bingo.dict.service.ISysDictDataService;
import com.bingo.study.common.core.controller.BaseController;
import com.bingo.study.common.core.web.response.RSX;
import com.bingo.study.common.core.web.response.RSXFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author h-bingo
 * @Date 2023-08-17 15:23
 * @Version 1.0
 */
@SuppressWarnings("unused")
@ResponseBody
@ConditionalOnMissingBean(SysDictDataController.class)
public class SysDictDataController extends BaseController {

    @Autowired
    private ISysDictDataService sysDictDataService;

    public RSX<SysDictData> save(HttpServletRequest request, HttpServletResponse response,
            @RequestBody SysDictData data) {

        return success(data);
    }

    public RSX<String> update(HttpServletRequest request, HttpServletResponse response) {

        return success();
    }

    public RSX<String> delete(HttpServletRequest request, HttpServletResponse response) {

        return success();
    }

    public RSX<String> ajax() {
        RSXFactory.success("");
        return RSXFactory.<String>builder().build();
    }
}
