package com.bingo.study.common.component.retry;

import com.bingo.study.common.core.utils.JsonMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * 重试机制
 */
@Slf4j
public class RetryFactory {

    /**
     * 默认重试次数
     */
    public static final int DAFAULT_TIME = 3;

    /**
     * 任务重试
     *
     * @param retryService
     * @return
     * @throws Throwable
     */
    public static Object retry(RetryService retryService) throws Throwable {
        return retry(retryService, DAFAULT_TIME);
    }


    /**
     * 任务重试
     *
     * @param retryService
     * @param times
     * @return
     * @throws Throwable
     */
    public static Object retry(RetryService retryService, int times) throws Throwable {
        try {
            log.info("任务重试剩余次数：{}", times);
            Object exec = retryService.exec();
            log.info("任务执行结果：{}", JsonMapper.getInstance().toJsonString(exec));
            return exec;
        } catch (Throwable e) {
            if (times > 1) {
                return retry(retryService, --times);
            } else {
                log.info("任务重试执行失败");
                throw e;
            }
        }
    }
}
