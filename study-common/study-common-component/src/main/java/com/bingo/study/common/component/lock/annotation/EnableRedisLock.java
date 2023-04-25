package com.bingo.study.common.component.lock.annotation;

import com.bingo.study.common.component.lock.aspect.RedisLockAspect;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启 {@link RedisLock} 注解功能
 *
 * @Author h-bingo
 * @Date 2023-04-24 17:39
 * @Version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({RedisLockAspect.class})
public @interface EnableRedisLock {
}
