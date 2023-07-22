package com.bingo.study.common.core.utils.validate;

import cn.hutool.core.lang.Assert;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @Author h-bingo
 * @Date 2023-04-13 17:10
 * @Version 1.0
 */
@Data
public class ValidateField {

    private String fieldName;

    private String fieldDesc;

    private Integer minLength;

    private Integer maxLength;

    private Long max;

    private Long min;

    private String regex;

    private List<ValidateType> validateTypeList;

    public ValidateField(String fieldName, String fieldDesc, Integer minLength, Integer maxLength, Long max, Long min,
            String regex, ValidateType... validateTypes) {
        Assert.notBlank(fieldName);

        this.fieldName = fieldName;
        this.fieldDesc = StringUtils.isBlank(fieldDesc) ? fieldName : fieldDesc;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.max = max;
        this.min = min;
        this.regex = regex;
        this.validateTypeList = Arrays.asList(validateTypes);
    }
}
