package com.bingo.study.common.component.lock.annotation;

import com.bingo.study.common.component.lock.LockType;

import java.lang.annotation.*;

/**
 * @Author h-bingo
 * @Date 2023-04-24 16:59
 * @Version 1.0
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLock {

    /**
     * 值为 true时 ：取方法的第一个参数作为加锁key值的一部分，此时方法第一个参数必须传加锁对象的唯一id值，对对象进行加锁
     * 值为 false时：任何情况都对整个方法加锁
     * <p>
     * 例如：对一件商品进行秒杀，需要传入商品id，商品id作为锁key值的一部分
     *
     * @return
     */
    boolean hasId() default false;

    /**
     * 锁类型
     *
     * @return
     */
    LockType lockType() default LockType.SYNC;

    /**
     * redis key 前缀，为空默认取类名+方法名+参数
     *
     * @return
     */
    String key() default "";

    /**
     * 最大等待时间，单位：ms，默认100秒
     *
     * @return
     */
    long waitTime() default 100 * 1000;

    /**
     * 锁持有时间, 超时自定解锁，单位：ms， 默认10秒
     * <p>
     * 锁持有时间，超出设定时间后将自定解锁，不会自动续期
     *
     * @return
     */
    long leaseTime() default 10 * 1000;
}
