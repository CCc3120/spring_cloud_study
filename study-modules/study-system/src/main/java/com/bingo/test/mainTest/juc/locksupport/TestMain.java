package com.bingo.test.mainTest.juc.locksupport;

import lombok.SneakyThrows;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 线程中断
 * <p>
 * 一个线程不应该由
 * <p>
 * 如何中断一个运行中的线程?
 * 1.通过 volatile 变量, 协商两个线程的停止
 * 2.通过 AtomicBoolean 变量, 协商两个线程的停止
 * 3.通过 Thread 自带的中断 api 实例方法实现
 * <p>
 * 如何停止一个运行中的线程?
 * <p>
 * 当前线程的中断标识为true, 是不是线程就立刻停止?
 * 不会立刻停止, thread.interrupt() 仅仅设置一个中断标志位
 * 0.wait/join/sleep 中断异常和清楚标志位演示
 * 1.线程正常运行
 * 2.另一个线程 2 秒后设置中断标识
 * 3.线程因为 sleep 出现中断异常, 并且清楚上一步设置的中断标识位, 导致线程标志位被复位(还原成未中断), 导致线程继续执行循环
 * 4.在 catch 中再次设置线程中断, 此时线程被设置中断标识位, 在下次循环中被跳出
 * <p>
 * 静态方法Thread.interrupted();谈谈理解?
 * 判断线程是否被中断, 并清除当前中断状态
 * 1.返回当前线程的中断状态, 测试当前线程是否已被中断
 * 2.将当前线程的中断状态清零并重新设为 false , 清除线程的中断状态
 * <p>
 *
 * @author h-bingo
 * @date 2023/09/20 22:26
 **/
public class TestMain {
    public static void main(String[] args) throws InterruptedException {
        TestMain main = new TestMain();

        main.test07();
    }

    void test07() throws InterruptedException {
        // 判断线程是否被中断, 并清除当前中断状态
        // 1.返回当前线程的中断状态, 测试当前线程是否已被中断
        // 2.将当前线程的中断状态清零并重新设为 false , 清除线程的中断状态
        // Thread.interrupted();
        System.out.println(Thread.currentThread().getName() + "\t" + Thread.interrupted());
        System.out.println(Thread.currentThread().getName() + "\t" + Thread.interrupted());
        Thread.currentThread().interrupt();
        System.out.println(Thread.currentThread().getName() + "\t" + Thread.interrupted());
        System.out.println(Thread.currentThread().getName() + "\t" + Thread.interrupted());
    }

    void test06() throws InterruptedException {
        //  wait/join/sleep 中断异常和清楚标志位演示
        // 1.线程正常运行
        // 2.另一个线程 2 秒后设置中断标识
        // 3.线程因为 sleep 出现中断异常, 并且清楚上一步设置的中断标识位, 导致线程标志位被复位(还原成未中断), 导致线程继续执行循环
        // 4.在 catch 中再次设置线程中断, 此时线程被设置中断标识位, 在下次循环中被跳出
        Thread thread = new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (true) {
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println(Thread.currentThread().getName() + "\t 被设置中断");
                        break;
                    }

                    try {
                        Thread.currentThread().sleep(200);
                    } catch (InterruptedException exception) {
                        // Thread.currentThread().interrupt(); // catch 中再次设置中断
                        exception.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + "\t 运行中");
                }
            }
        }, "t1");
        thread.start();

        Thread.sleep(2000);

        new Thread(new Runnable() {
            @Override
            public void run() {
                thread.interrupt();
            }
        }, "t2").start();
    }

    void test05() throws InterruptedException {
        //  thread.interrupt() 仅仅设置一个中断标志位, 不会立即中断
        Thread thread = new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (true) {
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println(Thread.currentThread().getName() + "\t 被设置中断");
                    } else {
                        System.out.println(Thread.currentThread().getName() + "\t 运行中");
                    }
                }
            }
        }, "t1");
        thread.start();

        Thread.sleep(2000);

        new Thread(new Runnable() {
            @Override
            public void run() {
                thread.interrupt();
            }
        }, "t2").start();
    }

    void test04() throws InterruptedException {
        // Thread 自带的中断 api 实例方法实现
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println(Thread.currentThread().getName() + "\t 被中断");
                        break;
                    }
                    System.out.println(Thread.currentThread().getName() + "\t 运行中");
                }
            }
        }, "t1");
        thread.start();


        Thread.sleep(2000);

        new Thread(new Runnable() {
            @Override
            public void run() {
                thread.interrupt();
            }
        }, "t2").start();
    }

    AtomicBoolean stop = new AtomicBoolean(false);

    void test03() throws InterruptedException {
        // AtomicBoolean 中断运行中的线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (stop.get()) {
                        System.out.println(Thread.currentThread().getName() + "\t 被中断");
                        break;
                    }
                    System.out.println(Thread.currentThread().getName() + "\t 运行中");
                }
            }
        }, "t1").start();

        Thread.sleep(2000);

        new Thread(new Runnable() {
            @Override
            public void run() {
                stop.set(true);
                // stop.compareAndSet(false, true);
            }
        }, "t2").start();
    }

    volatile boolean isStop = false;

    void test02() throws InterruptedException {
        // volatile 中断运行中的线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (isStop) {
                        System.out.println(Thread.currentThread().getName() + "\t 被中断");
                        break;
                    }
                    System.out.println(Thread.currentThread().getName() + "\t 运行中");
                }
            }
        }, "t1").start();

        Thread.sleep(2000);

        new Thread(new Runnable() {
            @Override
            public void run() {
                isStop = true;
            }
        }, "t2").start();
    }

    void test01() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }, "a");

        thread.start();

        // 仅仅是设置线程的中断状态为 true, 发起一个协商而不会立刻停止线程
        thread.interrupt();

        // 判断线程是否被中断, 并清除当前中断状态
        // 1.返回当前线程的中断状态, 测试当前线程是否已被中断
        // 2.将当前线程的中断状态清零并重新设为 false , 清除线程的中断状态
        Thread.interrupted();

        // 判断当前线程是否被中断(通过检查中断标志位), 和 Thread.interrupted(); 底层掉的一个方法, 清除参数不同
        thread.isInterrupted();
    }
}
