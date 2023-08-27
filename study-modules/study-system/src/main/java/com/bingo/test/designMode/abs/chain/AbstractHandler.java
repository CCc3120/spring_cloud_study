package com.bingo.test.designMode.abs.chain;

/**
 * @Author h-bingo
 * @Date 2023-08-25 14:35
 * @Version 1.0
 */
public abstract class AbstractHandler {

    private AbstractHandler next;

    public void setNext(AbstractHandler next) {
        this.next = next;
    }

    public AbstractHandler getNext() {
        return this.next;
    }

    public int handler() {
        int score = play();
        System.out.println("当前分钟：" + score);
        if (score >= 80) {
            if (getNext() != null) {
                return getNext().handler();
            }
        }
        return score;
    }

    public abstract int play();
}
