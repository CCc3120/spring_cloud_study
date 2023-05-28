package com.bingo.study.common.component.limiter;

/**
 * {@link LimitType#USER}
 * 根据用户限流时需要自定义实现该接口，获取当前登陆用户id，否则抛出异常
 *
 * @Author h-bingo
 * @Date 2023-05-15 10:22
 * @Version 1.0
 */
public interface LimitUserFactory {

    String getUserId();
}
