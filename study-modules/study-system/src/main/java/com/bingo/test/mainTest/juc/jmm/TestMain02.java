package com.bingo.test.mainTest.juc.jmm;

/**
 * volatile 两大特性
 * 1.可见性
 * 2.有序性(进制指定重排)
 * <p>
 * 内存屏障
 * <p>
 * public native void loadFence(); // 读屏障
 * <p>
 * public native void storeFence(); // 写屏障
 * <p>
 * public native void fullFence(); // 读写屏障
 *
 * @author h-bingo
 * @date 2023/09/23 14:16
 **/
public class TestMain02 {


    public static void main(String[] args) throws InterruptedException {
        test03();
    }

    static void test03() throws InterruptedException {
        // 禁止指令重排案例
        new VolatileSort().write();
        new VolatileSort().read();
    }

    static class VolatileSort {
        int i = 0;

        volatile boolean flag = false;

        public void write() {
            // 第二个是volatile写, 禁止重排

            i = 2; // 普通写/读
            // 此处插入 storestore 屏障
            flag = true; // volatile 写
            // 此处插入 storeload 屏障
        }

        public void read() {
            if (flag) { // volatile 读
                // 在此插入 loadload 屏障
                // 在此插入 loadstore 屏障
                System.out.println("i = " + i); // 普通读
            }
        }
    }

    static void test02() throws InterruptedException {
        // 无原子性案例
        MyNumber myNumber = new MyNumber();

        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 1000; j++) {
                        myNumber.addNumber();
                    }
                }
            }, i + "").start();
        }

        Thread.sleep(5000);

        System.out.println(myNumber.number);
    }

    static class MyNumber {
        volatile int number;

        public void addNumber() {
            number++;
        }
    }


    volatile static boolean flag = false;

    static void test01() throws InterruptedException {
        // 可见性案例

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "\t come in");
                while (!flag) {

                }
                System.out.println(Thread.currentThread().getName() + "\t come out");
            }
        }, "t1").start();

        Thread.sleep(2000);

        flag = true;
        System.out.println(Thread.currentThread().getName() + "\t end");
    }
}
