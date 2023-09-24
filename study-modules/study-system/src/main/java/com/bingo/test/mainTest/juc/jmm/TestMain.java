package com.bingo.test.mainTest.juc.jmm;

import lombok.SneakyThrows;

/**
 * JMM 内存模型
 * 三大特性:
 * 1.可见性
 * 指变量被修改后, 立即通知其他使用该变量的线程, 变量已被修改, 重新读取最新值(脏读)
 * 2.原子性
 * 指同一个操作是不可被打断的, 即多线程环境下, 操作不能被其他线程干扰
 * 3.有序性(指令重排序)
 *
 * @author h-bingo
 * @date 2023/09/22 22:53
 **/
public class TestMain {
    public static void main(String[] args) throws InterruptedException {
        test01();
    }

    static void test01() throws InterruptedException {
        // 指令重拍演示

        int x = 11; // 编号 1
        int y = 12; // 编号 2
        x = x + 5; // 编号 3
        y = x * x; // 编号 4

        // 1234

        // 2134

        // 1324

        // 问语句4可以重排后变成第一条吗?
        // 不能, 因为不满足数据依懒性, 未定义相关变量

        Thread thread = new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {

                Thread.sleep(3000);

                System.out.println("--------end----");
            }
        });

        thread.start();
        System.out.println(thread.isAlive());
        Thread.sleep(4000);
        System.out.println(thread.isAlive());
    }
}
