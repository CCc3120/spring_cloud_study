package com.bingo.dict.controller;

import com.bingo.study.common.core.controller.BaseController;
import com.bingo.study.common.core.web.response.RSX;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author h-bingo
 * @Date 2023-08-17 17:32
 * @Version 1.0
 */
@RestController
@RequestMapping(path = "/dict")
public class TestController extends BaseController {

    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public RSX<String> test(){

        return success();
    }
}
