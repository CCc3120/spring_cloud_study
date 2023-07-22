package com.bingo.test.job;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import tech.powerjob.worker.PowerJobSpringWorker;
import tech.powerjob.worker.common.PowerJobWorkerConfig;
import tech.powerjob.worker.common.constants.StoreStrategy;

/**
 * @Author h-bingo
 * @Date 2023-06-02 19:09
 * @Version 1.0
 */
// @Configuration
public class PowerJobWorkerConfiguration {

    @Bean
    public PowerJobSpringWorker initPowerJobWorker() throws Exception {

        // 1. 创建配置文件
        PowerJobWorkerConfig config = new PowerJobWorkerConfig();
        config.setPort(27777);
        config.setAppName("powerjob-worker-samples");
        config.setServerAddress(Lists.newArrayList("127.0.0.1:7700"));
        // 如果没有大型 Map/MapReduce 的需求，建议使用内存来加速计算
        config.setStoreStrategy(StoreStrategy.DISK);

        // 2. 创建 Worker 对象，设置配置文件（注意 Spring 用户需要使用 PowerJobSpringWorker，而不是 PowerJobWorker）
        PowerJobSpringWorker worker = new PowerJobSpringWorker(config);
        return worker;
    }
}
