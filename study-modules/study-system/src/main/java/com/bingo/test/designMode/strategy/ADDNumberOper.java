package com.bingo.test.designMode.strategy;

/**
 * @Author h-bingo
 * @Date 2023-07-28 10:52
 * @Version 1.0
 */
public class ADDNumberOper implements NumberOper {

    @Override
    public double doOper(double a, double b) {
        return a + b;
    }
}
