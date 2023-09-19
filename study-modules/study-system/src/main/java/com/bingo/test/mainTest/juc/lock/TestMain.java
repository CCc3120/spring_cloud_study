package com.bingo.test.mainTest.juc.lock;

import lombok.SneakyThrows;

/**
 * @author h-bingo
 * @date 2023/09/17 15:44
 **/
public class TestMain {
    public static void main(String[] args) throws Exception {
        // 8 锁案例演示
        // test01();
        // test02();
        // test03();
        // test04();
        // test05();
        // test06();
        // test07();
        test08();
    }

    static synchronized void test08() throws InterruptedException {
        // 2个对象  1个静态方法 1个普通方法
        // 一个对象锁 一个类锁, 两个锁对象不同, 不会产生争论
        Phone phone = new Phone();
        Phone phone2 = new Phone();

        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                phone.sendEmail();
            }
        }, "a").start();

        Thread.sleep(200);

        new Thread(new Runnable() {
            @Override
            public void run() {
                phone2.sendSMS();
            }
        }, "b").start();
    }

    static synchronized void test07() throws InterruptedException {
        // 1个对象  1个静态方法 1个普通方法
        // 一个对象锁 一个类锁, 两个锁对象不同, 不会产生争论
        Phone phone = new Phone();

        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                phone.sendEmail();
            }
        }, "a").start();

        Thread.sleep(200);

        new Thread(new Runnable() {
            @Override
            public void run() {
                phone.sendSMS();
            }
        }, "b").start();
    }

    static synchronized void test06() throws InterruptedException {
        // 2个对象 2个静态方法
        // 类锁 按类的实例调用顺序执行
        // 且会锁住该类中所有的带 synchronized 的静态方法, 其他方法除外
        Phone phone = new Phone();
        Phone phone2 = new Phone();

        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                phone.sendEmail();
            }
        }, "a").start();

        Thread.sleep(200);

        new Thread(new Runnable() {
            @Override
            public void run() {
                phone2.sendSMS();
            }
        }, "b").start();
    }

    static synchronized void test05() throws InterruptedException {
        // 一个对象 两个静态方法
        // 类锁 按类的实例调用顺序执行
        // 且会锁住该类中所有的带 synchronized 的静态方法, 其他方法除外
        Phone phone = new Phone();

        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                phone.sendEmail();
            }
        }, "a").start();

        Thread.sleep(200);

        new Thread(new Runnable() {
            @Override
            public void run() {
                phone.sendSMS();
            }
        }, "b").start();
    }

    static synchronized void test04() throws InterruptedException {
        // 两个 Phone 对象
        // 2个对象 两个非静态方法
        // 对象锁 互不影响
        Phone phone = new Phone();
        Phone phone2 = new Phone();

        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                phone.sendEmail();
            }
        }, "a").start();

        Thread.sleep(200);

        new Thread(new Runnable() {
            @Override
            public void run() {
                phone2.sendSMS();
            }
        }, "b").start();
    }

    static synchronized void test03() throws InterruptedException {
        // 在 sendEmail 中睡眠 3 秒
        // 1个对象 两个非静态方法
        // 对象锁 按对象调用顺序执行
        // 且会锁住该对象中所有的带 synchronized 的非静态方法, 其他方法除外（非synchronized方法不受影响）
        Phone phone = new Phone();

        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                phone.sendEmail();
            }
        }, "a").start();

        Thread.sleep(200);

        new Thread(new Runnable() {
            @Override
            public void run() {
                phone.hello();
            }
        }, "b").start();
    }

    static synchronized void test02() throws InterruptedException {
        // 在 sendEmail 中睡眠 3 秒
        // 1个对象 两个非静态方法
        // 对象锁 按对象调用顺序执行
        // 且会锁住该对象中所有的带 synchronized 的非静态方法, 其他方法除外
        Phone phone = new Phone();

        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                phone.sendEmail();
            }
        }, "a").start();

        Thread.sleep(200);

        new Thread(new Runnable() {
            @Override
            public void run() {
                phone.sendSMS();
            }
        }, "b").start();
    }

    static synchronized void test01() throws InterruptedException {
        // 1个对象 两个非静态方法
        // 对象锁 按对象调用顺序执行
        // 且会锁住该对象中所有的带 synchronized 的非静态方法, 其他方法除外
        Phone phone = new Phone();

        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                phone.sendEmail();
            }
        }, "a").start();

        Thread.sleep(200);

        new Thread(new Runnable() {
            @Override
            public void run() {
                phone.sendSMS();
            }
        }, "b").start();
    }

    static class Phone {

        // ---test05---
        public static synchronized void sendEmail() throws InterruptedException {
            // public synchronized void sendEmail() throws InterruptedException {
            // ---test02---
            Thread.sleep(3000);

            System.out.println("---sendEmail---");

        }

        // ---test05---
        // public static synchronized void sendSMS() {
        public synchronized void sendSMS() {

            System.out.println("---sendSMS---");

        }

        public void hello() {
            // ---test03---
            System.out.println("---hello---");
        }
    }
}
