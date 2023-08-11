package com.bingo.study.common.component.dict.translate.annotation;

import com.bingo.study.common.component.dict.translate.util.TranslateUtil;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启 {@link Translate} 功能（数据字典或枚举翻译）
 *
 * @Author h-bingo
 * @Date 2023-04-23 15:43
 * @Version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({TranslateUtil.class})
public @interface EnableTranslate {
}
