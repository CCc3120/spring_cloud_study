package com.bingo.study.common.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程工具类
 *
 * @author bingo
 * @date 2022-04-28 16:06
 */
@Slf4j
public class ThreadUtil {

    private static final ExecutorService DEFAULT_THREAD_POOL = Executors.newFixedThreadPool(20);

    /**
     * sleep等待,单位为毫秒
     */
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
        }
    }

    /**
     * 数据并行处理
     *
     * @param list
     * @param handle
     * @param <T>
     */
    public static <T> ProcessResult<String> groupHandle(List<T> list, ThreadHandle handle,
            ExecutorService executorService) {
        CountDownLatch downLatch = new CountDownLatch(list.size());
        list.forEach(t -> executorService.submit(() -> {
            handle.handle(t);
            downLatch.countDown();
        }));
        try {
            downLatch.await();
            return ProcessResult.success();
        } catch (InterruptedException e) {
            log.error("数据分组处理异常", e);
            return ProcessResult.fail("数据分组处理异常");
        }
    }

    public static <T> ProcessResult<String> groupHandle(List<T> list, ThreadHandle handle, int batchSize,
            ExecutorService executorService) {
        List<List<T>> groupBySize = ArrayUtil.groupBySize(list, batchSize);
        return groupHandle(groupBySize, handle, executorService);
    }

    public static <T> ProcessResult<String> groupHandle(List<T> list, ThreadHandle handle, int batchSize) {
        return groupHandle(list, handle, batchSize, DEFAULT_THREAD_POOL);
    }
}
