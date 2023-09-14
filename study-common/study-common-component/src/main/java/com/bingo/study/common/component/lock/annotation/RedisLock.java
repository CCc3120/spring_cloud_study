package com.bingo.study.common.component.lock.annotation;

import com.bingo.study.common.component.lock.LockType;

import java.lang.annotation.*;

/**
 * redis锁注解
 *
 * @Author h-bingo
 * @Date 2023-04-24 16:59
 * @Version 1.0
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLock {

    /**
     * 值为 true时 ：取 {@link LockKey} 标记的参数作为加锁key值的一部分
     * 配合 {@link LockKey} 或 {@link RedisLock#paramName()} 使用， {@link RedisLock#paramName()} 的优先级高
     * <p>
     * 值为 false时：任何情况都对整个方法加锁
     * <p>
     * 例如：对一件商品进行秒杀，需要传入商品id，商品id作为锁key值的一部分
     *
     * @return
     */
    boolean singleton() default false;

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
    String keyPrefix() default "";

    /**
     * 通过EL表达式解析参数作为 redis key 的一部分
     * <p>
     * 一般用户对某个具体的数据进行加锁
     *
     * @return
     */
    String paramName() default "";

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
