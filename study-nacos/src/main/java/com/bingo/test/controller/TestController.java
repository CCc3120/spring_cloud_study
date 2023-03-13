package com.bingo.test.controller;

import com.bingo.test.component.ReflushProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TestController
 * @Description TODO
 * @Author h-bingo
 * @Date 2022-12-20 16:00
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping(path = "/test")
public class TestController {

    @Autowired
    private ReflushProperties reflushProperties;

    @RequestMapping(method = RequestMethod.GET)
    public String test(String name) {
        log.info("TestController被调用了。。。");
        return reflushProperties.getName() + ":" + reflushProperties.getOrder();
    }
}
