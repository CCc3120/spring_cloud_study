package com.bingo.test.designMode.abs;

/**
 * @Author h-bingo
 * @Date 2023-08-25 11:13
 * @Version 1.0
 */
public class TestMain {
    public static void main(String[] args) {
        TestService testService = new TestService();

        testService.getResp("你好");
    }
}
