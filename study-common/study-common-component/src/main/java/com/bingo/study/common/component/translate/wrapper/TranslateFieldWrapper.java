package com.bingo.study.common.component.translate.wrapper;

import com.bingo.study.common.component.translate.constant.TranslateType;
import com.bingo.study.common.core.enums.CodeDescEnum;
import lombok.Data;

import java.lang.reflect.Field;

/**
 * @Author h-bingo
 * @Date 2023-04-24 14:07
 * @Version 1.0
 */
@Data
public class TranslateFieldWrapper {

    /**
     * 翻译类型
     */
    private TranslateType translateType;

    /**
     * 枚举信息
     */
    private Class<? extends CodeDescEnum> enumClass;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 翻译字段
     */
    private Field field;

    /**
     * 翻译结果填充字段
     */
    private Field fullField;
}
