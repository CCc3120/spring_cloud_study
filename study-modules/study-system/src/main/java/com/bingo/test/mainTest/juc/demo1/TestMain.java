package com.bingo.test.mainTest.juc.demo1;

/**
 * @author h-bingo
 * @date 2023/09/16 13:11
 **/
public class TestMain {
    public static void main(String[] args) {

        test02();
    }

    static void test02() {
        // main 线程也是用户线程, 若没有用户线程, 则 jvm 会退出程序
        Thread thread = new Thread(() -> {
            while (true) {
            }
        }, "t1");

        // 守护线程
        // thread.setDaemon(true);

        thread.start();

        Thread thread1 = new Thread(() -> {
            while (true) {
            }
        }, "t2");

        thread1.setDaemon(true);

        thread1.start();
    }

    static void test01() {
        new Thread(() -> {

        }, "t1").start();
    }
}
