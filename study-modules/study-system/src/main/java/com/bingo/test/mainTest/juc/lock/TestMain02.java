package com.bingo.test.mainTest.juc.lock;

/**
 * 锁的字节码反编译
 * <p>
 * javap -c xx.class
 * <p>
 * javap -v xx.class
 *
 * @author h-bingo
 * @date 2023/09/17 15:44
 **/
public class TestMain02 {

    Object object = new Object();

    public void m1() {
        synchronized (object) {
            System.out.println("---------sync code block-----------");
            throw new RuntimeException();
        }
    }

    public synchronized void m2() {
        System.out.println("---------sync method-----------");
    }

    public static synchronized void m3() {
        System.out.println("---------sync static method-----------");
    }

    public static void main(String[] args) throws Exception {

    }
}
