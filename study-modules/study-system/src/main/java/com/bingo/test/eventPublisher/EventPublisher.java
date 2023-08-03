package com.bingo.test.eventPublisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * @Author h-bingo
 * @Date 2023-07-24 14:24
 * @Version 1.0
 */
@Service
public class EventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void testEvent(){
        TestEvent testEvent = new TestEvent();

        testEvent.setName("123asd");
        applicationEventPublisher.publishEvent(testEvent);
    }
}
