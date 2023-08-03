package com.bingo.test.retry;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 * @Author h-bingo
 * @Date 2023-07-24 15:05
 * @Version 1.0
 */
@Service
public class RetryService {

    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
    public void retryDemo(int a) {

        System.out.println("执行");
        throw new RuntimeException();
    }

    @Recover
    public void recover(Exception e, int a) {
        System.out.println("异常:" + a + e.getMessage());
        e.printStackTrace();
    }
}
