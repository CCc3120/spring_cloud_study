package com.bingo.test.job;

import lombok.extern.slf4j.Slf4j;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;

/**
 * @Author h-bingo
 * @Date 2023-06-02 17:02
 * @Version 1.0
 */
@Slf4j
// @Component
public class MyTestJob implements BasicProcessor {

    @Override
    public ProcessResult process(TaskContext taskContext) throws Exception {
        log.info("执行定时任务～");

        return new ProcessResult(true, "MyTestJob process successfully");
    }
}
