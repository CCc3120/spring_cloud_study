package com.bingo.test.mainTest.juc.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 可重入锁(递归锁):
 * 指在同一个线程在外层方法获取锁的时候, 再进入该方法的内层方法会自动获取锁 (前提锁对象是同一个对象),
 * 不会因为之前已经获取过还没释放而阻塞
 * <p>
 * 如果是 1 个有 synchronized 修饰的递归调用方法, 程序第二次进入被自己阻塞了岂不是天大的笑话, 出现了作茧自缚.
 * 所以Java中 ReentrantLock 和 synchronized 都是可重入锁, 可重入锁的一个优点是可一定程度避免死锁
 * <p>
 * 可重入锁种类:
 * synchronized: 隐式锁, 默认是可重入锁
 * 原理: 每个锁对象拥有一个锁计数器和一个指向持有该锁的线程的指针
 * 当 monitorenter 时, 如果目标锁对象的计数器为零, 那么说明他没有被其他线程所持有, Java虚拟机会将该锁对象的持有线程设置
 * 为当前线程, 并且将计数器加 1.
 * 在目标锁对象的计数器不为零的情况下, 如果锁对象的持有线程是当前线程, 那么Java虚拟机可以将其计数器加1, 否则需要等待,
 * 直至持有锁的线程释放锁.
 * 当执行 monitorexit 时, Java虚拟机则将锁对象计数器减 1, 计数器为零代表锁已释放
 * <p>
 * ReentrantLock: 显示锁, lock.lock(); 和  lock.unlock(); 必须要一一配对, 否则会出现死锁
 *
 * @author h-bingo
 * @date 2023/09/20 19:38
 **/
public class TestMain04 {
    public static void main(String[] args) {
        TestMain04 t = new TestMain04();

        // t.test02();
        t.test05();
    }

    ReentrantLock lock = new ReentrantLock();

    void test05() {
        // ReentrantLock 可重入演示
        // lock.lock(); 和  lock.unlock(); 必须要一一配对
        new Thread(() -> {
            lock.lock();

            try {
                System.out.println(Thread.currentThread().getName() + "-----------外层锁----------");
                lock.lock();

                try {
                    System.out.println(Thread.currentThread().getName() + "-----------中层锁----------");
                    lock.lock();
                    try {
                        System.out.println(Thread.currentThread().getName() + "-----------内层锁----------");

                    } finally {
                        lock.unlock();
                    }
                } finally {
                    lock.unlock();
                }
            } finally {
                lock.unlock();
            }
        }, "a").start();

        new Thread(() -> {
            lock.lock();

            try {
                System.out.println(Thread.currentThread().getName() + "-----------外层锁----------");
                lock.lock();

                try {
                    System.out.println(Thread.currentThread().getName() + "-----------中层锁----------");
                    lock.lock();
                    try {
                        System.out.println(Thread.currentThread().getName() + "-----------内层锁----------");

                    } finally {
                        lock.unlock();
                    }
                } finally {
                    lock.unlock();
                }
            } finally {
                lock.unlock();
            }
        }, "b").start();
    }

    synchronized void test02() {
        // 同步方法
        System.out.println(Thread.currentThread().getName() + "-----------外层锁----------");
        test03();
        System.out.println(Thread.currentThread().getName() + "-----------外层锁end----------");
    }

    synchronized void test03() {
        // 同步方法
        System.out.println(Thread.currentThread().getName() + "-----------中层锁----------");
        test04();
    }

    synchronized void test04() {
        // 同步方法
        System.out.println(Thread.currentThread().getName() + "-----------内层锁----------");
    }

    static void test01() {
        // 同步代码块
        Object o = new Object();

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (o) {
                    System.out.println(Thread.currentThread().getName() + "-----------外层锁----------");
                    synchronized (o) {
                        System.out.println(Thread.currentThread().getName() + "-----------中层锁----------");
                        synchronized (o) {
                            System.out.println(Thread.currentThread().getName() + "-----------内层锁----------");
                        }
                    }
                }
            }
        }, "a").start();
    }
}
