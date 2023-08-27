package com.bingo.test.demo;

import com.bingo.study.common.core.controller.BaseController;
import com.bingo.study.common.core.web.response.RSX;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author h-bingo
 * @Date 2023-08-23 15:07
 * @Version 1.0
 */
@RestController
@RequestMapping(path = "/test/2")
public class Test2Controller extends BaseController {

    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public RSX<String> test() {

        return success();
    }
}
