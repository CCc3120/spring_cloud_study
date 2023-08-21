package com.bingo.test.eventPublisher;

import com.bingo.study.common.core.web.page.AjaxResult;
import com.bingo.study.common.core.web.page.AjaxResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author h-bingo
 * @Date 2023-07-24 14:27
 * @Version 1.0
 */
@RestController
@RequestMapping(path = "/event")
public class EventController {

    @Autowired
    private EventPublisher eventPublisher;

    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public AjaxResult<Void> event(){

        eventPublisher.testEvent();

        return AjaxResultFactory.success();
    }
}
