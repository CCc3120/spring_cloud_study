package com.bingo.test.mainTest.juc.lock;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 公平锁和非公平锁
 * <p>
 * 公平锁:
 * 指多个线程按照申请顺序来获取锁
 * ReentrantLock lock = new ReentrantLock(true); // 公平锁
 * <p>
 * 非公平锁:
 * 指线程获取锁的顺序不是按照线程申请顺序获取的, 有可能后申请的比先申请的优先获取锁, 在高并发的环境下, 有可能造成优先级
 * 翻转或饥饿状态(某个线程一直获取不到锁)
 * ReentrantLock lock = new ReentrantLock(); // 非公平锁
 * synchronized 也是非公平锁, 目的是提升性能, 它不能保证先请求锁的线程先获取锁
 * <p>
 * 为什么会有非公平锁？为什么默认非公平锁？
 * 1、恢复挂起的线程到真正锁的获取还是有时间差的，从开发人员来看这个时间微乎其微，但是从CPU的角度来看，这个时间差存在的
 * 还是很明显的，所以非公平锁更能充分利用CPU的时间片，尽量减少CPU的空闲状态时间
 * 2、使用多线程很重要的考量点是线程切换的开销，当采用非公平锁时，当 1 个线程请求锁获取同步状态，然后释放同步状态，所以刚
 * 释放锁的线程在此刻再次获取同步状态的概率就会变得非常大，所以就较少了线程的开销
 * <p>
 * 什么时候用公平锁？什么时候用非公平锁？
 * 如果为了更高的吞吐量，很显然非公平锁是比较合适的，因为节省很多线程切换时间，吞吐量自然就上去了。
 * 否则就用公平锁，大家公平使用
 *
 * @author h-bingo
 * @date 2023/09/20 19:09
 **/
public class TestMain03 {

    public static void main(String[] args) throws IOException {
        demo01();

    }

    public static void demo01() {
        Ticket ticket = new Ticket();

        new Thread(() -> {
            for (int i = 0; i < 55; i++) {
                ticket.sale();
            }
        }, "a").start();

        new Thread(() -> {
            for (int i = 0; i < 55; i++) {
                ticket.sale();
            }
        }, "b").start();

        new Thread(() -> {
            for (int i = 0; i < 55; i++) {
                ticket.sale();
            }
        }, "c").start();
    }
}

class Ticket {
    private int number = 50;

    // 无参构造, 默认非公平锁
    // ReentrantLock lock = new ReentrantLock(); // 非公平锁
    ReentrantLock lock = new ReentrantLock(true); // 公平锁

    public void sale() {
        lock.lock();

        try {
            if (number > 0) {
                System.out.println(Thread.currentThread().getName() + "卖出第: \t" + (number--) + "\t 还剩下: " + number);
            }
        } finally {
            lock.unlock();
        }
    }
}