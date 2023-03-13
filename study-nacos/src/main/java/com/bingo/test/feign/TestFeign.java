package com.bingo.test.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName TestFeign
 * @Description TODO
 * @Author h-bingo
 * @Date 2022-12-20 16:50
 * @Version 1.0
 */
@FeignClient(contextId = "testFeign",name = "study-nacos", path = "/test")
public interface TestFeign {

    @RequestMapping(method = RequestMethod.GET)
    String test(@RequestParam(name = "name") String name);
}
