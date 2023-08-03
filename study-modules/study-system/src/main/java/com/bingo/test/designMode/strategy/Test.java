package com.bingo.test.designMode.strategy;

/**
 * @Author h-bingo
 * @Date 2023-07-28 10:53
 * @Version 1.0
 */
public class Test {
    public static void main(String[] args) {
        double a = 1.2;

        double b = 1.4;

        double v = new DefaultNumberOper(new ADDNumberOper()).doOper(a, b);

        System.out.println(v);
    }
}
