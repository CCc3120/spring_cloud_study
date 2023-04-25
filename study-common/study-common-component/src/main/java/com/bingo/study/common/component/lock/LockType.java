package com.bingo.study.common.component.lock;

import com.bingo.study.common.core.enums.DescEnum;

/**
 * 锁类型
 *
 * @Author h-bingo
 * @Date 2023-04-25 10:20
 * @Version 1.0
 */
public enum LockType implements DescEnum {
    /**
     * 有一个线程加锁，其他线程直接结束
     * <p>
     * 此时需注意评估好程序执行时间，若锁持有时间小于程序执行时间，则会提前释放锁，可能引起锁失效问题
     */
    MUTEX("互斥锁"),
    /**
     * 有一个线程加锁，则等待一定时间
     * <p>
     * 此时需注意评估好程序执行时间，若锁持有时间小于程序执行时间，则会提前释放锁，可能引起锁失效问题
     */
    SYNC("同步锁"),
    /**
     * 有一个线程加锁，其他线程直接结束
     * <p>
     * 无需设置锁持有时间，需注意评估好程序不会一直卡死的问题，否则会出现锁无法释放，慎用
     */
    AUTO_RENEWAL_MUTEX("互斥锁(自动续期)"),
    /**
     * 有一个线程加锁，则等待一定时间
     * <p>
     * 无需设置锁持有时间，需注意评估好程序不会一直卡死的问题，否则会出现锁无法释放，慎用
     */
    AUTO_RENEWAL_SYNC("同步锁(自动续期)"),
    ;

    private String desc;

    LockType(String desc) {
        this.desc = desc;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
