package com.bingo.test.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.*;

/**
 * @Author h-bingo
 * @Date 2023-05-15 14:49
 * @Version 1.0
 */
public class FutureTest {

    public static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // test1();
        //
        // test2();
        //
        // test3();
        //
        // test4();
        //
        // test5();
        //
        // test6();
        //
        // test7();
        //
        // test8();


        List<String> orderNo = new ArrayList<>();

        Map<String,String> map = new HashMap<>();
        for (String s : orderNo) {
            boolean flag = true;// 假如s加锁成功
            if(flag){
                map.put(s,"加锁成功的key");
            }
        }

        for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
            // 执行
            // 释放锁
        }


        CompletableFuture<Boolean> booleanCompletableFuture = CompletableFuture.supplyAsync(new Supplier<Boolean>() {
            @Override
            public Boolean get() {

                return false;
            }
        }).whenComplete(new BiConsumer<Boolean, Throwable>() {
            @Override
            public void accept(Boolean aBoolean, Throwable throwable) {


            }
        });



        CompletableFuture<Object> objectCompletableFuture = CompletableFuture.supplyAsync(new Supplier<Object>() {
            @Override
            public Object get() {
                System.out.println(Thread.currentThread().getName());
                return "2";
            }
        }).whenComplete(new BiConsumer<Object, Throwable>() {
            @Override
            public void accept(Object o, Throwable throwable) {
                System.out.println(Thread.currentThread().getName() + "||" + o);
            }
        }).thenCompose(new Function<Object, CompletionStage<Object>>() {
            @Override
            public CompletionStage<Object> apply(Object o) {

                return CompletableFuture.supplyAsync(new Supplier<Object>() {
                    @Override
                    public Object get() {
                        System.out.println(Thread.currentThread().getName());
                        return "3";
                    }
                });
            }
        }).thenCombineAsync(CompletableFuture.supplyAsync(new Supplier<Object>() {
            @Override
            public Object get() {
                System.out.println(Thread.currentThread().getName());
                return "4";
            }
        }), new BiFunction<Object, Object, Object>() {
            @Override
            public Object apply(Object o, Object o2) {
                System.out.println(Thread.currentThread().getName() + "||" + o + "||" + o2);
                return "5";
            }
        });

        System.out.println(Thread.currentThread().getName() + "||" + objectCompletableFuture.get());
    }

    public static void test1() throws ExecutionException, InterruptedException {
        long timeMillis = System.currentTimeMillis();
        System.out.println(timeMillis);
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            Thread.sleep(1000);
            return "test1";
        });

        new Thread(futureTask).start();
        System.out.println(futureTask.get() + ":" + (System.currentTimeMillis() - timeMillis));
    }

    public static void test2() throws ExecutionException, InterruptedException {
        long timeMillis = System.currentTimeMillis();
        System.out.println(timeMillis);
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }, "test2");

        new Thread(futureTask).start();
        System.out.println(futureTask.get() + ":" + (System.currentTimeMillis() - timeMillis));
    }

    public static void test3() throws ExecutionException, InterruptedException {
        long timeMillis = System.currentTimeMillis();
        System.out.println(timeMillis);
        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
            return "test3";
        }).whenComplete((s, throwable) -> {
            System.out.println(s + ":" + (System.currentTimeMillis() - timeMillis));
        });
    }

    public static void test4() throws ExecutionException, InterruptedException {
        long timeMillis = System.currentTimeMillis();
        System.out.println(timeMillis);
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
            return "test4";
        }).handle((s, throwable) -> {
            System.out.println(s + ":" + (System.currentTimeMillis() - timeMillis));
            return s;
        });

        System.out.println(completableFuture.get() + ":" + (System.currentTimeMillis() - timeMillis));
    }

    public static void test5() throws ExecutionException, InterruptedException {
        long timeMillis = System.currentTimeMillis();
        System.out.println(timeMillis);
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
            return "test5";
        }).thenCompose(s -> {
            return CompletableFuture.supplyAsync(() -> {
                return s + "test5-2";
            });
        }).thenApply(new Function<String, String>() {
            @Override
            public String apply(String s) {
                return s + "test5-3";
            }
        }).whenComplete(new BiConsumer<String, Throwable>() {
            @Override
            public void accept(String s, Throwable throwable) {
                System.out.println(s + ":" + (System.currentTimeMillis() - timeMillis));
            }
        });
    }

    public static void test6() throws ExecutionException, InterruptedException {
        long timeMillis = System.currentTimeMillis();
        System.out.println(timeMillis);
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {
                    }
                    return "test6";
                }, executorService)
                .thenCombine(CompletableFuture.supplyAsync(new Supplier<String>() {
                    @Override
                    public String get() {
                        return "test6-2";
                    }
                }), new BiFunction<String, String, String>() {
                    @Override
                    public String apply(String s, String s2) {
                        return s + s2;
                    }
                }).whenComplete(new BiConsumer<String, Throwable>() {
                    @Override
                    public void accept(String s, Throwable throwable) {
                        System.out.println(s + ":" + (System.currentTimeMillis() - timeMillis));
                    }
                });
    }

    public static void test7() throws ExecutionException, InterruptedException {
        long timeMillis = System.currentTimeMillis();
        System.out.println(timeMillis);
        CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {
                    }
                    return "test7";
                }, executorService)

                .thenAcceptBoth(CompletableFuture.supplyAsync(new Supplier<String>() {
                    @Override
                    public String get() {
                        return "test7-2";
                    }
                }), new BiConsumer<String, String>() {
                    @Override
                    public void accept(String s, String s2) {
                        System.out.println(s + s2 + ":" + (System.currentTimeMillis() - timeMillis));
                    }
                });
    }

    public static void test8() throws ExecutionException, InterruptedException {
        long timeMillis = System.currentTimeMillis();
        System.out.println(timeMillis);
        CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {
                    }
                    return "test8";
                }, executorService)
                .acceptEither(CompletableFuture.supplyAsync(new Supplier<String>() {
                    @Override
                    public String get() {
                        return "test8-2";
                    }
                }), new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        System.out.println(s + ":" + (System.currentTimeMillis() - timeMillis));
                    }
                })

        // .whenComplete(new BiConsumer<String, Throwable>() {
        //     @Override
        //     public void accept(String s, Throwable throwable) {
        //         System.out.println(s + ":" + (System.currentTimeMillis() - timeMillis));
        //     }
        // })
        ;
    }

    public static void test9() throws ExecutionException, InterruptedException {
        long timeMillis = System.currentTimeMillis();
        System.out.println(timeMillis);
        CompletableFuture.anyOf(CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                return "test9";
            }
        }), CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                return "test9-2";
            }
        }), CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                return "test9-3";
            }
        })).whenComplete(new BiConsumer<Object, Throwable>() {
            @Override
            public void accept(Object o, Throwable throwable) {
                System.out.println(o + ":" + (System.currentTimeMillis() - timeMillis));
            }
        })

        // .whenComplete(new BiConsumer<String, Throwable>() {
        //     @Override
        //     public void accept(String s, Throwable throwable) {
        //         System.out.println(s + ":" + (System.currentTimeMillis() - timeMillis));
        //     }
        // })
        ;
    }

    public static void test10() throws ExecutionException, InterruptedException {
        long timeMillis = System.currentTimeMillis();
        System.out.println(timeMillis);

    }
}
