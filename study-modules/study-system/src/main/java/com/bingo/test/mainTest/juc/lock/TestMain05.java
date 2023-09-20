package com.bingo.test.mainTest.juc.lock;

import lombok.SneakyThrows;

/**
 * 死锁案例演示:
 * 另一种方式
 * ReentrantLock: 显示锁, lock.lock(); 和  lock.unlock(); 必须要一一配对, 否则会出现死锁
 * <p>
 * 如何排查死锁:
 * 命令:
 * jps -l  // 查看java进程号
 * jstack [进程号] 查看详细信息
 *
 * 图形化: jconsole
 *
 * @author h-bingo
 * @date 2023/09/20 21:26
 **/
public class TestMain05 {
    public static void main(String[] args) {
        Object a = new Object();
        Object b = new Object();

        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                synchronized (a) {
                    System.out.println(Thread.currentThread().getName() + "\t 获得a锁");
                    Thread.sleep(1000);
                    synchronized (b) {
                        System.out.println(Thread.currentThread().getName() + "\t 成功获得b锁");
                    }
                }
            }
        }, "t1").start();

        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                synchronized (b) {
                    System.out.println(Thread.currentThread().getName() + "\t 获得b锁");
                    Thread.sleep(1000);
                    synchronized (a) {
                        System.out.println(Thread.currentThread().getName() + "\t 成功获得a锁");
                    }
                }
            }
        }, "t2").start();
    }
}
