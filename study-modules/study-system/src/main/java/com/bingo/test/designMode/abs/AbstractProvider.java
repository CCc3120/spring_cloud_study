package com.bingo.test.designMode.abs;

/**
 * @Author h-bingo
 * @Date 2023-08-25 11:19
 * @Version 1.0
 */
public abstract class AbstractProvider {

    protected <T> void executeCommand(String key, Runnable runnable, T defaultValue) {
        runnable.run();
    }
}
