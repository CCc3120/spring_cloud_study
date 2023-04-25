package com.bingo.study.common.component.returnValue;

import com.bingo.study.common.core.enums.CodeDescEnum;

import java.util.Arrays;

/**
 * 默认忽略的属性
 *
 * @Author h-bingo
 * @Date 2023-04-21 17:30
 * @Version 1.0
 */
public enum IgnoreField implements CodeDescEnum<String> {

    CREATE_TIME("fdCreateTime", "创建时间"),
    UPDATE_TIME("fdUpdateTime", "修改时间"),
    ;

    private final String code;

    private final String desc;

    IgnoreField(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public static String[] fields() {
        return Arrays.stream(values()).map(IgnoreField::getCode).toArray(String[]::new);
    }
}
