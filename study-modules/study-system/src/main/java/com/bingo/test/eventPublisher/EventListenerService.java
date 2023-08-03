package com.bingo.test.eventPublisher;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * @Author h-bingo
 * @Date 2023-07-24 14:29
 * @Version 1.0
 */
@Service
public class EventListenerService implements SmartInitializingSingleton, InitializingBean {

    @EventListener(classes = {TestEvent.class})
    public void eventListenerTest(Object obj) {

        System.out.println(obj);
    }

    @EventListener(classes = {TestEvent.class})
    public void eventListenerTest01(Object obj) {

        System.out.println(obj);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 每个bean实例化完成都会调用
    }

    @Override
    public void afterSingletonsInstantiated() {
        // 所有的单例（没有@Lazy）bean实例化完之后统一执行
    }
}
