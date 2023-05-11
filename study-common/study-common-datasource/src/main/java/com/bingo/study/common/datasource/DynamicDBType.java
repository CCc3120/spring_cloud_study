package com.bingo.study.common.datasource;

import com.bingo.study.common.core.enums.CodeDescEnum;

/**
 * 数据源类型，对应配置文件中的数据源配置
 *
 * @Author h-bingo
 * @Date 2023-04-26 16:32
 * @Version 1.0
 */
public enum DynamicDBType implements CodeDescEnum<String> {

    MASTER("master", "主数据源"),

    SLAVE("slave", "从数据源"),

    ;


    private String code;
    private String desc;

    DynamicDBType(String code, String desc) {
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
}
