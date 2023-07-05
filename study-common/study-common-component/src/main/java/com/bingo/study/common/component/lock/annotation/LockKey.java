package com.bingo.study.common.component.lock.annotation;

import java.lang.annotation.*;

/**
 * 标记参数为 lockKey 的一部分
 *
 * @Author h-bingo
 * @Date 2023-06-08 14:40
 * @Version 1.0
 */
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.PARAMETER})
public @interface LockKey {

    /**
     * key的别名
     *
     * @return
     */
    String alias() default "";
}
