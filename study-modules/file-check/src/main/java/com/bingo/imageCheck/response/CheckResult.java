package com.bingo.imageCheck.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author h-bingo
 * @Date 2023-08-07 14:40
 * @Version 1.0
 */
@Data
public class CheckResult {

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("结果")
    private Boolean result;

    @ApiModelProperty("提示")
    private String message;

    @ApiModelProperty("时间")
    private String dateTime;

    @ApiModelProperty("拍摄设备")
    private String software;

    @ApiModelProperty("纬度")
    private String latitude;

    @ApiModelProperty("经度")
    private String longitude;

    public static CheckResult success(String fileName, String message) {
        CheckResult checkResult = new CheckResult();
        checkResult.setResult(true);
        message = StringUtils.isBlank(message) ? "文件未被修改" : message;
        checkResult.setMessage(message);
        checkResult.setFileName(fileName);
        checkResult.setLongitude("未知");
        checkResult.setLatitude("未知");
        checkResult.setSoftware("未知");
        checkResult.setDateTime("未知");
        return checkResult;
    }

    public static CheckResult fail(String fileName, String message) {
        CheckResult checkResult = new CheckResult();
        checkResult.setResult(false);
        message = StringUtils.isBlank(message) ? "文件被修改" : message;
        checkResult.setMessage(message);
        checkResult.setFileName(fileName);
        checkResult.setLongitude("未知");
        checkResult.setLatitude("未知");
        checkResult.setSoftware("未知");
        checkResult.setDateTime("未知");
        return checkResult;
    }
}
