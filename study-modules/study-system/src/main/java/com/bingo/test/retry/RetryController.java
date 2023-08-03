package com.bingo.test.retry;

import com.bingo.study.common.core.page.AjaxResult;
import com.bingo.study.common.core.page.AjaxResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author h-bingo
 * @Date 2023-07-24 15:05
 * @Version 1.0
 */
@RestController
@RequestMapping(path = "/retry")
public class RetryController {


    @Autowired
    private RetryService retryService;


    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public AjaxResult<Void> test(){

        retryService.retryDemo(2);
        return AjaxResultFactory.success();
    }
}
