package com.bingo.study.common.component.retry;

import com.bingo.study.common.core.utils.JsonMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

/**
 * 重试机制
 */
@Slf4j
public class RetryFactory {

    /**
     * 默认重试次数
     */
    public static final int DEFAULT_TIME = 3;

    /**
     * 任务重试
     *
     * @param supplier
     * @return
     */
    public static <T> T retry(Supplier<T> supplier) {
        return retry(supplier, DEFAULT_TIME);
    }

    /**
     * 任务重试
     *
     * @param supplier
     * @param times
     * @return
     */
    public static <T> T retry(Supplier<T> supplier, int times) {
        try {
            log.info("任务重试剩余次数：{}", times);
            T t = supplier.get();
            log.info("任务执行结果：{}", JsonMapper.getInstance().toJsonString(t));
            return t;
        } catch (Exception e) {
            if (times > 1) {
                return retry(supplier, --times);
            } else {
                log.info("任务重试执行失败");
                throw e;
            }
        }
    }

    public static void main(String[] args) {
        String retry = retry(() -> {
            throw new RuntimeException();
        });
    }
}
