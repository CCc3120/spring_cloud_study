package com.bingo.test.demo;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author h-bingo
 * @Date 2023-06-28 15:17
 * @Version 1.0
 */
@Component
public class TestTimer implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        new Thread(() -> {
            Timer timer = new Timer();
            System.out.println("启动" + System.currentTimeMillis());
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + "执行了" + System.currentTimeMillis());
                }
            }, 1000 * 5);
        }).start();

    }
}
