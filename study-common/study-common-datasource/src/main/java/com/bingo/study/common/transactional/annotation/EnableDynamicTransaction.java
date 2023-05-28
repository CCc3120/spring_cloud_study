package com.bingo.study.common.transactional.annotation;

import com.bingo.study.common.transactional.aspect.DynamicTransactionAspect;
import com.bingo.study.common.transactional.config.DynamicTransactionConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author h-bingo
 * @Date 2023-04-28 14:53
 * @Version 1.0
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({DynamicTransactionConfig.class, DynamicTransactionAspect.class})
public @interface EnableDynamicTransaction {
}
