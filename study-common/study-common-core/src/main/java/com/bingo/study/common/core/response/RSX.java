package com.bingo.study.common.core.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author h-bingo
 * @Date 2023-08-18 15:47
 * @Version 1.0
 */
@Data
@ApiModel("响应结果")
public class RSX<T> implements Serializable {

    @ApiModelProperty("响应状态")
    private String status;

    @ApiModelProperty("响应提示")
    private String message;

    @ApiModelProperty("响应数据")
    private T data;

    @ApiModelProperty("错误码")
    private String errCode;

    @ApiModelProperty("错误提示")
    private String errMsg;

    @ApiModelProperty("操作模块")
    private String module;

    protected RSX() {
    }

    protected RSX(String status, String message, T data, String errCode, String errMsg, String module) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.module = module;
    }

    public boolean success() {
        return RSXStatusEnum.SUCCESS_STATUS.getCode().equals(this.status);
    }
}
