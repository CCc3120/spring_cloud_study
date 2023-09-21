package com.bingo.test.mainTest.transmittableThreadLocal;

import com.alibaba.ttl.TtlRunnable;
import com.alibaba.ttl.threadpool.TtlExecutors;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * ThreadLocal 不能跨线程传递值
 * <p>
 * InheritableThreadLocal 由当前线程创建的线程，将会集成当前线程 ThreadLocal 中保存的值，
 * 当使用线程池的时候，出现线程复用，线程不一定是当前线程创建，这时候就无法传递 ThreadLocal 中保存的值
 * <p>
 * TransmittableThreadLocal 继承了 InheritableThreadLocal， 自然有 InheritableThreadLocal 的效果，
 * 要解决 InheritableThreadLocal 线程复用的问题，需要配合 TtlRunnable、TtlCallable 或 TtlExecutors 使用
 *
 * @Author h-bingo
 * @Date 2023-09-20 16:05
 * @Version 1.0
 */
public class TestMain {

    static ExecutorService executorService = Executors.newFixedThreadPool(1);
    static ExecutorService asyncExecutorService = Executors.newFixedThreadPool(1);
    static ExecutorService ttlExecutorService = TtlExecutors.getTtlExecutorService(Executors.newFixedThreadPool(1));

    // static CountDownLatch asyncCtl = new CountDownLatch(1);

    static {
        // new Thread(() -> {
        //     asyncExecutorService.execute(() -> {
        //         System.out.println("初次创建异步线程池");
        //     });
        //     ttlExecutorService = TtlExecutors.getTtlExecutorService(asyncExecutorService);
        //     ttlExecutorService.execute(() -> {
        //         System.out.println("-------------");
        //     });
        //     asyncCtl.countDown();
        // }).start();
    }

    public static void main(String[] args) throws InterruptedException {
        // try {
        //     asyncCtl.await();
        // } catch (InterruptedException e) {
        // }

        asyncExecutorService.execute(() -> {
            System.out.println("asyncExecutorService 初始化");
        });
        // ttlExecutorService = TtlExecutors.getTtlExecutorService(asyncExecutorService);
        ttlExecutorService.execute(() -> {
            System.out.println("ttlExecutorService 初始化");
        });

        Common.setName("main-name");
        Common.setKey("main-key");
        Common.setCodec("main-codec");

        System.out.println("main线程获取变量值");
        System.out.println(Common.getName() + "," + Common.getKey() + "," + Common.getCodec());

        // CountDownLatch countDownLatch = new CountDownLatch(1);
        // 直接子线程 InheritableThreadLocal、TransmittableThreadLocal 都可以获取
        Thread thread1 = new Thread(() -> {
            System.out.println("main子线程获取变量值");
            System.out.println(Common.getName() + "," + Common.getKey() + "," + Common.getCodec());
            // countDownLatch.countDown();
        });
        thread1.start();
        thread1.join();

        CountDownLatch countDownLatch2 = new CountDownLatch(1);
        // 未初始化线程的 线程池 InheritableThreadLocal、TransmittableThreadLocal 都可以获取
        executorService.execute(() -> {
            System.out.println("executorService 线程池获取变量值");
            System.out.println(Common.getName() + "," + Common.getKey() + "," + Common.getCodec());
            countDownLatch2.countDown();
        });
        countDownLatch2.await();


        CountDownLatch countDownLatch3 = new CountDownLatch(1);
        // 已初始化线程的 都不可以获取
        // asyncExecutorService.execute(() -> {
        //     System.out.println("asyncExecutorService 线程池获取变量值");
        //     System.out.println(Common.getName() + "," + Common.getKey() + "," + Common.getCodec());
        //     countDownLatch3.countDown();
        // });

        asyncExecutorService.execute(TtlRunnable.get(new Runnable() {
            @Override
            public void run() {
                System.out.println("asyncExecutorService 线程池获取变量值");
                System.out.println(Common.getName() + "," + Common.getKey() + "," + Common.getCodec());
                countDownLatch3.countDown();
            }
        }, false, true));
        countDownLatch3.await();

        CountDownLatch countDownLatch4 = new CountDownLatch(1);
        // 已初始化线程的 TransmittableThreadLocal 可以获取
        ttlExecutorService.execute(() -> {
            System.out.println("ttlExecutorService 线程池获取变量值");
            System.out.println(Common.getName() + "," + Common.getKey() + "," + Common.getCodec());
            countDownLatch4.countDown();
        });
        countDownLatch4.await();

        CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                System.out.println("CompletableFuture01 线程池获取变量值");
                System.out.println(Common.getName() + "," + Common.getKey() + "," + Common.getCodec());
                return null;
            }
        }).join();

        CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                System.out.println("CompletableFuture02 线程池获取变量值");
                System.out.println(Common.getName() + "," + Common.getKey() + "," + Common.getCodec());
                return null;
            }
        }).join();
    }
}
