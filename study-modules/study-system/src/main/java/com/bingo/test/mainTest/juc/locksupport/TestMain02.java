package com.bingo.test.mainTest.juc.locksupport;

import lombok.SneakyThrows;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * LockSupport
 * LockSupport 中的 park 和 unpark 的作用分别是阻塞线程和解除阻塞线程
 * 3 种线程阻塞和唤醒的方式
 * * 1.object 中提供的 wait 和 notify/notifyAll
 * * 2.Condition 接口中的 await 和 signal
 * * 3.LockSupport 类的 park 和 unpark
 *
 * @author h-bingo
 * @date 2023/09/21 22:28
 **/
public class TestMain02 {
    public static void main(String[] args) throws InterruptedException {
        test03();
    }

    static void test03() throws InterruptedException {
        // LockSupport 类的 park 和 unpark
        // 底层调用 unsafe 的 native 方法
        // park 和 unpark 方法没有使用顺序限制(只限单次 park 的情况)
        // unpark 最多累计一次, 多次 park 需要多次 unpark 解除, 多次 park 的情况, park 和 unpark 必须保证顺序
        Thread t1 = new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {

                System.out.println(Thread.currentThread().getName() + "\t come in");

                LockSupport.park();
                // LockSupport.park();

                System.out.println(Thread.currentThread().getName() + "\t 被唤醒");

            }
        }, "a");
        t1.start();

        Thread.sleep(1000);

        new Thread(new Runnable() {
            @Override
            public void run() {

                LockSupport.unpark(t1);
                // LockSupport.unpark(t1);
                System.out.println(Thread.currentThread().getName() + "\t 通知唤醒");

            }
        }, "b").start();
    }

    static void test02() throws InterruptedException {
        // Condition 接口中的 await 和 signal
        // await 和 signal 必须放在 lock.lock() 和 lock.unlock() 块中或者 synchronized 块中, 要先获取锁, 不然会报错
        // 必须先 await 再 signal/signalAll, 否则程序无法被唤醒
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                System.out.println(Thread.currentThread().getName() + "\t come in");

                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
                System.out.println(Thread.currentThread().getName() + "\t 被唤醒");

            }
        }, "a").start();

        Thread.sleep(1000);

        new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();

                try {
                    condition.signal();
                    System.out.println(Thread.currentThread().getName() + "\t 通知唤醒");
                } finally {
                    lock.unlock();
                }
            }
        }, "b").start();
    }

    static void test01() throws InterruptedException {
        //  wait 和 notify/notifyAll 都必须放在同步代码里面: 同步代码块\同步实例方法\同步静态方法都可以, 不然会报错
        // 必须先 wait 再 notify/notifyAll, 否则程序无法被唤醒
        Object object = new Object();

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (object) {
                    System.out.println(Thread.currentThread().getName() + "\t come in");

                    try {
                        object.wait(); // 必须写在同步代码里面 三种都可以
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println(Thread.currentThread().getName() + "\t 被唤醒");
                }
            }
        }, "a").start();

        Thread.sleep(1000);

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (object) {
                    object.notify();
                    System.out.println(Thread.currentThread().getName() + "\t 通知唤醒");
                }
            }
        }, "b").start();
    }
}
