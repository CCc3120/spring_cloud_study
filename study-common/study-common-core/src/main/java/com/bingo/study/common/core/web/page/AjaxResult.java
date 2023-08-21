package com.bingo.study.common.core.web.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author bingo
 * @date 2022-03-24 10:34
 */
@Data
@ApiModel("响应结果")
@Deprecated
public class AjaxResult<T> implements Serializable {

    @ApiModelProperty("状态码")
    private String code;

    @ApiModelProperty("和code绑定的枚举提示")
    private String message;

    @ApiModelProperty("错误提示")
    private String errMsg;

    @ApiModelProperty("响应数据")
    private T data;

    protected AjaxResult() {
    }

    protected AjaxResult(String code, String message, String errMsg, T data) {
        this.code = code;
        this.message = message;
        this.errMsg = errMsg;
        this.data = data;
    }

    public boolean success() {
        return HttpStatusEnum.SUCCESS.getCode().equals(this.code);
    }
}
