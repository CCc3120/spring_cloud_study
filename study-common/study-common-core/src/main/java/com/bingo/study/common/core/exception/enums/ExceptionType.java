package com.bingo.study.common.core.exception.enums;

import com.bingo.study.common.core.enums.CodeDescEnum;
import lombok.Getter;

/**
 * @Author h-bingo
 * @Date 2023-08-09 16:38
 * @Version 1.0
 */
@Getter
public enum ExceptionType implements CodeDescEnum<String> {


    ;


    private final String code;

    private final String desc;

    ExceptionType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
