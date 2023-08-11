package com.bingo.study.common.core.exception;

import com.bingo.study.common.core.exception.enums.ExceptionType;
import lombok.Getter;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常基类
 *
 * @Author h-bingo
 * @Date 2023-08-09 14:49
 * @Version 1.0
 */
@Getter
public abstract class BaseException extends RuntimeException {

    /**
     * 异常模块
     */
    private String module;

    /**
     * 异常代码
     */
    private String code;

    /**
     * 异常描述
     */
    private String desc;

    public BaseException(String message, String module, ExceptionType exceptionType) {
        super(message);
        this.module = module;
        this.code = exceptionType.getCode();
        this.desc = exceptionType.getDesc();
    }

    /***
     * 返回堆栈内容到字符串
     * @Param []
     * @Return java.lang.String
     * @Date 2023-08-09 14:53
     */
    public String getStackTraceMessage() {
        StringWriter writer = new StringWriter();
        this.printStackTrace(new PrintWriter(writer, true));
        return writer.getBuffer().toString();
    }
}
