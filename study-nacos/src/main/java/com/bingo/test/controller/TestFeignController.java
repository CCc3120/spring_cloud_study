package com.bingo.test.controller;

import com.bingo.test.feign.TestFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * @ClassName TestFeignController
 * @Description TODO
 * @Author h-bingo
 * @Date 2022-12-20 16:54
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping(path = "/test")
public class TestFeignController {

    @Autowired
    private TestFeign testFeign;

    @RequestMapping(path = "/feign", method = RequestMethod.GET)
    public String test(String value) {
        log.info("TestFeignController被调用了。。。。。");
        return testFeign.test(value);
    }

}

