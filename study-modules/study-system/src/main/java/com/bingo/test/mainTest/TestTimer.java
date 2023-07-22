package com.bingo.test.mainTest;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author h-bingo
 * @Date 2023-06-28 14:40
 * @Version 1.0
 */
public class TestTimer {


    public static void main(String[] args) {
        Timer timer = new Timer();
        System.out.println("启动" + System.currentTimeMillis());
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "执行了" + System.currentTimeMillis());
            }
        }, 1000 * 1);
        // timer.s


        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

        scheduler.initialize();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 2);
        System.out.println("启动" + System.currentTimeMillis());
        scheduler.schedule(() -> {
            System.out.println(Thread.currentThread().getName() + "执行了" + System.currentTimeMillis());
        }, calendar.getTime());
    }
}