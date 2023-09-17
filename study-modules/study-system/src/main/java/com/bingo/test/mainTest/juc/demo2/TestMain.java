package com.bingo.test.mainTest.juc.demo2;

import lombok.Getter;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author h-bingo
 * @date 2023/09/17 11:43
 **/
public class TestMain {
    public static void main(String[] args) throws Exception {
        // Student student = new Student();
        // student.setName("").setSex("");

        test13();
    }

    static void test13() throws Exception {
        CompletableFuture<String> supplyAsync1 = CompletableFuture
                .supplyAsync(new Supplier<String>() {
                    @SneakyThrows
                    @Override
                    public String get() {
                        Thread.sleep(1000);
                        return "success1";
                    }
                })
                .thenCompose(new Function<String, CompletionStage<String>>() {
                    @Override
                    public CompletionStage<String> apply(String s) {

                        return CompletableFuture.supplyAsync(new Supplier<String>() {
                            @Override
                            public String get() {
                                return "success2";
                            }
                        });
                    }
                });
        System.out.println(supplyAsync1.join());
    }

    static void test12() throws Exception {
        // 对多个计算结果进行合并 thenCombine()
        CompletableFuture<String> supplyAsync1 = CompletableFuture
                .supplyAsync(new Supplier<String>() {
                    @SneakyThrows
                    @Override
                    public String get() {
                        Thread.sleep(1000);
                        return "success1";
                    }
                });
        CompletableFuture<String> supplyAsync2 = CompletableFuture
                .supplyAsync(new Supplier<String>() {
                    @SneakyThrows
                    @Override
                    public String get() {
                        Thread.sleep(800);
                        System.out.println(Thread.currentThread().getName());
                        return "success2";
                    }
                });

        CompletableFuture<String> stringCompletableFuture = supplyAsync1.thenCombine(supplyAsync2, new BiFunction<String, String, String>() {
            @Override
            public String apply(String s, String s2) {
                return s + "\t" + s2;
            }
        });
        System.out.println(stringCompletableFuture.join());
    }

    static void test11() throws Exception {
        // 对处理速度进行选择 applyToEither()
        CompletableFuture<String> supplyAsync1 = CompletableFuture
                .supplyAsync(new Supplier<String>() {
                    @SneakyThrows
                    @Override
                    public String get() {
                        Thread.sleep(1000);
                        return "success1";
                    }
                });

        CompletableFuture<String> supplyAsync2 = CompletableFuture
                .supplyAsync(new Supplier<String>() {
                    @SneakyThrows
                    @Override
                    public String get() {
                        Thread.sleep(800);
                        System.out.println(Thread.currentThread().getName());
                        return "success2";
                    }
                });

        CompletableFuture<String> toEither = supplyAsync1.applyToEither(supplyAsync2, new Function<String, String>() {
            @Override
            public String apply(String s) {
                System.out.println(s);
                return s;
            }
        });
        System.out.println(toEither.join());
    }

    static void test10() throws Exception {
        // 默认的全部是 ForkJoinPool.commonPool 线程池
        // Async不带这个后缀的方法用的线程和上一个线程是同一个,
        // Async带这个后缀的方法, 若没有指定线程池, 则默认使用 ForkJoinPool.commonPool 线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        CompletableFuture
                .supplyAsync(new Supplier<String>() {
                    @SneakyThrows
                    @Override
                    public String get() {
                        Thread.sleep(1000);
                        System.out.println(Thread.currentThread().getName());
                        return "success";
                    }
                }, threadPool)
                .thenRunAsync(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(Thread.currentThread().getName());
                        // return s + "t1";
                    }
                })
                .thenRun(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(Thread.currentThread().getName());
                        // return s + "t2";
                    }
                })
                .thenRun(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(Thread.currentThread().getName());
                    }
                })
        ;
        System.in.read();
    }

    static void test09() throws Exception {
        // 对计算结果进行消费 thenAccept
        CompletableFuture
                .supplyAsync(new Supplier<String>() {
                    @SneakyThrows
                    @Override
                    public String get() {
                        Thread.sleep(3000);
                        return "success";
                    }
                })
                // 不依赖上一步结果, 两个任务类似串行
                .thenRun(new Runnable() {
                    @Override
                    public void run() {

                    }
                })
                // 消费上一步结果无返回值
                .thenAccept(new Consumer<Void>() {
                    @Override
                    public void accept(Void unused) {

                    }
                })
                // 消费上一步结果并且有返回值
                .thenApply(new Function<Void, String>() {
                    @Override
                    public String apply(Void unused) {
                        return null;
                    }
                });

        CompletableFuture<Void> supplyAsync = CompletableFuture.supplyAsync(new Supplier<String>() {
            @SneakyThrows
            @Override
            public String get() {
                Thread.sleep(3000);
                return "success";
            }
        }).thenAccept(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        });

        System.in.read();
    }

    static void test08() throws Exception {
        // 对计算结果进行处理
        // thenApply 发生异常中断, 执行 exceptionally
        // handle 发生异常可以继续下一步, 根据带的异常参数可以进下一步处理, handle 异常只能传递一次
        // CompletableFuture<String> supplyAsync = CompletableFuture.supplyAsync(new Supplier<String>() {
        //     @SneakyThrows
        //     @Override
        //     public String get() {
        //         Thread.sleep(3000);
        //         return "success";
        //     }
        // }).thenApply(new Function<String, String>() {
        //     @Override
        //     public String apply(String s) {
        //         System.out.println(s);
        //         return s + "\t t1";
        //     }
        // }).thenApply(new Function<String, String>() {
        //     @Override
        //     public String apply(String s) {
        //         System.out.println(s);
        //         return s + "\t t2";
        //     }
        // }).whenComplete(new BiConsumer<String, Throwable>() {
        //     @Override
        //     public void accept(String s, Throwable throwable) {
        //         if (throwable == null) {
        //             System.out.println(s);
        //         }
        //     }
        // }).exceptionally(new Function<Throwable, String>() {
        //     @Override
        //     public String apply(Throwable throwable) {
        //         System.out.println(throwable.getMessage());
        //         return null;
        //     }
        // });

        CompletableFuture<String> supplyAsync = CompletableFuture.supplyAsync(new Supplier<String>() {
            @SneakyThrows
            @Override
            public String get() {
                Thread.sleep(3000);
                return "success";
            }
        }).handle(new BiFunction<String, Throwable, String>() {
            @Override
            public String apply(String s, Throwable throwable) {
                System.out.println(s);
                int a = 1 / 0;
                return s + "\t t1";
            }
        }).handle(new BiFunction<String, Throwable, String>() {
            @Override
            public String apply(String s, Throwable throwable) {
                System.out.println(s);
                int a = 1 / 0;
                return s + "\t t2";
            }
        }).whenComplete(new BiConsumer<String, Throwable>() {
            @Override
            public void accept(String s, Throwable throwable) {
                if (throwable == null) {
                    System.out.println(s);
                } else {
                    System.out.println(throwable.getMessage());
                }
            }
        }).exceptionally(new Function<Throwable, String>() {
            @Override
            public String apply(Throwable throwable) {
                System.out.println(throwable.getMessage());
                return null;
            }
        });

        System.out.println(Thread.currentThread().getName() + "-------------end");
        System.in.read();
    }

    static void test07() throws Exception {
        // CompletionStage api 分组介绍
        // 获取结果 get() get(x,x) join() getNow(d)
        // 主动触发计算 complete()
        CompletableFuture<String> supplyAsync = CompletableFuture.supplyAsync(new Supplier<String>() {
            @SneakyThrows
            @Override
            public String get() {
                Thread.sleep(3000);
                return "success";
            }
        });
        // System.out.println(supplyAsync.get());
        // System.out.println(supplyAsync.get(2, TimeUnit.SECONDS));
        // System.out.println(supplyAsync.join());
        // System.out.println(supplyAsync.getNow("default"));
        supplyAsync.complete("complete"); // 是否打断get方法, 立即返回入参 成功打断返回 true 否则 false
        System.out.println(supplyAsync.join());
    }

    static void test06() throws Exception {
        // 1. 同一款产品, 查询各大电商平台的售价
        // 2. 同一款产品, 查询在同一个电商平台上, 各入驻卖家的售价
        String productName = "mysql";

        List<NetMall> priceList = Arrays.asList(
                new NetMall("jd"),
                new NetMall("tb"),
                new NetMall("tm")
        );

        long startTime = System.currentTimeMillis();

        // 串行计算
        // List<String> stringList = priceList
        //         .stream()
        //         .map(new Function<NetMall, String>() {
        //             @Override
        //             public String apply(NetMall netMall) {
        //                 return String.format(productName + " in %s price is %.2f", netMall.getNetMallName(), netMall.calcPrice(productName));
        //             }
        //         })
        //         .collect(Collectors.toList());
        // System.out.println(stringList);

        // 并行计算
        List<String> stringList = priceList
                .stream()
                .map(new Function<NetMall, CompletableFuture<String>>() {
                         @Override
                         public CompletableFuture<String> apply(NetMall netMall) {
                             return CompletableFuture.supplyAsync(new Supplier<String>() {
                                 @Override
                                 public String get() {
                                     return String.format(productName + " in %s price is %.2f", netMall.getNetMallName(), netMall.calcPrice(productName));
                                 }
                             });
                         }
                     }
                )
                .collect(Collectors.toList())
                .stream()
                .map(new Function<CompletableFuture<String>, String>() {
                    @Override
                    public String apply(CompletableFuture<String> stringCompletableFuture) {
                        return stringCompletableFuture.join();
                    }
                })
                .collect(Collectors.toList());
        System.out.println(stringList);

        System.out.println("耗时: " + (System.currentTimeMillis() - startTime));
    }

    static class NetMall {
        @Getter
        private String netMallName;

        public NetMall(String netMallName) {
            this.netMallName = netMallName;
        }

        public double calcPrice(String productName) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return ThreadLocalRandom.current().nextDouble(2) + productName.charAt(0);
        }
    }

    static void test05() throws Exception {
        CompletableFuture<String> supplyAsync = CompletableFuture.supplyAsync(new Supplier<String>() {
            @SneakyThrows
            @Override
            public String get() {
                Thread.sleep(500);
                System.out.println("supplyAsync \t" + Thread.currentThread().getName());
                // int a = 1 / 0;
                return "over";
            }
        });

        // join 也可以获取结果 也会阻塞
        // get会抛出检查型异常, join不会抛出检查异常, 但是发生异常该抛还是抛
        System.out.println(supplyAsync.join());
    }

    static void test04() throws Exception {
        // 默认 ForkJoinPool 是守护线程, 主线程结束后会关闭
        // 异常 whenComplete 和 exceptionally 都会执行
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        CompletableFuture<String> supplyAsync = CompletableFuture.supplyAsync(new Supplier<String>() {
            @SneakyThrows
            @Override
            public String get() {
                Thread.sleep(500);
                System.out.println("supplyAsync \t" + Thread.currentThread().getName());
                int a = 1 / 0;
                return "over";
            }
        }, threadPool).whenComplete(new BiConsumer<String, Throwable>() {
            @Override
            public void accept(String s, Throwable throwable) {
                if (throwable == null) {
                    System.out.println("上一步计算完成\t" + s);
                } else {
                    System.out.println("supplyAsync whenComplete 发生异常 \t" + throwable.getMessage());
                }
            }
        }).exceptionally(new Function<Throwable, String>() {
            // 异常回调函数
            @Override
            public String apply(Throwable throwable) {
                System.out.println("supplyAsync exceptionally 发生异常 \t" + throwable.getMessage());
                return "exceptionally";
            }
        });

        System.out.println(Thread.currentThread().getName() + "---end");
        System.out.println(supplyAsync.get());
    }

    static void test03() throws Exception {
        // 不指定线程池, 默认是 ForkJoinPool
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        // 无返回值
        CompletableFuture<Void> runAsync = CompletableFuture.runAsync(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                Thread.sleep(1000);
                System.out.println("runAsync \t" + Thread.currentThread().getName());
            }
        }, threadPool);

        // 有返回值
        CompletableFuture<String> supplyAsync = CompletableFuture.supplyAsync(new Supplier<String>() {
            @SneakyThrows
            @Override
            public String get() {
                Thread.sleep(500);
                System.out.println("supplyAsync \t" + Thread.currentThread().getName());
                return "over";
            }
        }, threadPool);

        // get 都会阻塞
        System.out.println(supplyAsync.get());
        System.out.println(runAsync.get());
    }

    static void test02() throws Exception {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        FutureTask<String> future = new FutureTask<>((Callable) () -> {
            Thread.sleep(1000);
            System.out.println("call: " + Thread.currentThread().getName());
            return "Callable1";
        });
        threadPool.submit(future);

        FutureTask<String> future2 = new FutureTask<>((Callable) () -> {
            Thread.sleep(1000);
            System.out.println("call: " + Thread.currentThread().getName());
            return "Callable2";
        });
        threadPool.submit(future2);

        FutureTask<String> future3 = new FutureTask<>((Callable) () -> {
            Thread.sleep(1000);
            System.out.println("call: " + Thread.currentThread().getName());
            return "Callable3";
        });
        threadPool.submit(future3);

        // future3.get()

        System.out.println(Thread.currentThread().getName());
    }

    // FutureTask
    static void test01() throws Exception {
        FutureTask<String> future = new FutureTask<>(new Callable() {
            @Override
            public String call() throws Exception {

                Thread.sleep(5000);
                System.out.println("call: " + Thread.currentThread().getName());
                return "Callable";
            }
        });
        Thread thread = new Thread(future);
        thread.start();
        System.out.println("main: " + Thread.currentThread().getName());
        System.out.println("Callable result: " + future.get(3, TimeUnit.SECONDS));


        while (true) {
            if (future.isDone()) {
                System.out.println("Callable result: " + future.get());
                break;
            } else {
                Thread.sleep(500);
            }
        }
        System.out.println("Callable result: " + future.isDone());
    }
}
