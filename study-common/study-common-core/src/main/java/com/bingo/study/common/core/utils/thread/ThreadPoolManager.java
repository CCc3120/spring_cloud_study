package com.bingo.study.common.core.utils.thread;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.alibaba.ttl.threadpool.TtlExecutors;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @Author h-bingo
 * @Date 2023-09-21 11:12
 * @Version 1.0
 */
@Component
@ConditionalOnMissingBean(ThreadPoolManager.class)
public class ThreadPoolManager implements DisposableBean {

    // 默认线程池
    private static final ExecutorService DEFAULT_THREAD_POOL =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(),
                    new ThreadFactoryBuilder().setNamePrefix("default-thread-pool-").build());

    // 调度型线程池
    private static final ScheduledExecutorService SCHEDULED_THREAD_POOL =
            Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors(),
                    new ThreadFactoryBuilder().setNamePrefix("schedule-thread-pool-").build());

    // ttl线程池，用于传递线程变量用
    private static final ExecutorService TTL_THREAD_POOL =
            TtlExecutors.getTtlExecutorService(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(),
                    new ThreadFactoryBuilder().setNamePrefix("ttl-thread-pool-").build()));

    public static ExecutorService getDefaultThreadPool() {
        return DEFAULT_THREAD_POOL;
    }

    public static ScheduledExecutorService getScheduledThreadPool() {
        return SCHEDULED_THREAD_POOL;
    }

    public static ExecutorService getTtlThreadPool() {
        return TTL_THREAD_POOL;
    }

    public static void shutdown() {
        DEFAULT_THREAD_POOL.shutdown();
        SCHEDULED_THREAD_POOL.shutdown();
        Objects.requireNonNull(TTL_THREAD_POOL).shutdown();
    }

    @Override
    public void destroy() throws Exception {
        shutdown();
    }
}
