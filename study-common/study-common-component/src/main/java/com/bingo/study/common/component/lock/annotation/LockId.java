package com.bingo.study.common.component.lock.annotation;

import java.lang.annotation.*;

/**
 * 加锁key参数注解
 *
 * @Author h-bingo
 * @Date 2023-06-08 14:40
 * @Version 1.0
 */
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.PARAMETER})
public @interface LockId {
}
