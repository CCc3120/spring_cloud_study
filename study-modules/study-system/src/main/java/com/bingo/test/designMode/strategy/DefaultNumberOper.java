package com.bingo.test.designMode.strategy;

/**
 * @Author h-bingo
 * @Date 2023-07-28 10:51
 * @Version 1.0
 */
public class DefaultNumberOper implements NumberOper {

    private NumberOper numberOper;

    public DefaultNumberOper(NumberOper numberOper) {
        this.numberOper = numberOper;
    }

    @Override
    public double doOper(double a, double b) {
        return numberOper.doOper(a, b);
    }
}
