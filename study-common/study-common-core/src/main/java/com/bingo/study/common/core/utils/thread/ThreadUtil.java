package com.bingo.study.common.core.utils.thread;

import com.bingo.study.common.core.utils.ArrayUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.IntFunction;

/**
 * 线程工具类
 *
 * @author bingo
 * @date 2022-04-28 16:06
 */
@Slf4j
public class ThreadUtil {

    /**
     * sleep等待,单位为毫秒
     */
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * 数据并行处理
     *
     * @param list            数据数据列表
     * @param executorService 线程池
     * @param handle          处理器
     * @param <T>
     */
    private static <T> void groupHandle(List<List<T>> list, ExecutorService executorService,
            GroupHandle<List<T>> handle) {
        log.info("数据分组处理：{} 组", list.size());
        CompletableFuture.allOf(
                list.stream()
                        .map(t -> CompletableFuture.runAsync(() -> handle.handle(t), executorService))
                        .toArray((IntFunction<CompletableFuture<Void>[]>) CompletableFuture[]::new)
        ).join();
        log.info("数据分组处理完成");
    }

    public static <T> void groupHandle(List<T> list, int batchSize, ExecutorService executorService,
            GroupHandle<List<T>> handle) {
        groupHandle(ArrayUtil.groupBySize(list, batchSize), executorService, handle);
    }

    public static <T> void groupHandle(List<T> list, int batchSize, GroupHandle<List<T>> handle) {
        groupHandle(list, batchSize, ThreadPoolManager.getDefaultThreadPool(), handle);
    }
}
