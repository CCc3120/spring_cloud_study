package com.bingo.study.common.component.deprecatedInterface.annotation;

import com.bingo.study.common.component.deprecatedInterface.aspect.DeprecatedInterfaceSeeAspect;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启 {@link DeprecatedInterfaceSee} 注解功能
 *
 * @Author h-bingo
 * @Date 2023-04-23 17:59
 * @Version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({DeprecatedInterfaceSeeAspect.class})
public @interface EnableDeprecatedInterfaceSee {
}
