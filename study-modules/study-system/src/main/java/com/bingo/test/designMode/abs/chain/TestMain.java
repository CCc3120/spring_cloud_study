package com.bingo.test.designMode.abs.chain;

/**
 * @Author h-bingo
 * @Date 2023-08-25 14:42
 * @Version 1.0
 */
public class TestMain {
    public static void main(String[] args) {
        FirstHandler firstHandler = new FirstHandler();

        SecondHandler secondHandler = new SecondHandler();

        ThirdHandler thirdHandler = new ThirdHandler();

        firstHandler.setNext(secondHandler);

        secondHandler.setNext(thirdHandler);

        firstHandler.handler();
    }
}
