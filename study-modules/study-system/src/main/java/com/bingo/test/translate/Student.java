package com.bingo.test.translate;

import com.bingo.study.common.component.dict.translate.annotation.Translate;
import com.bingo.study.common.component.dict.translate.constant.TranslateType;
import com.bingo.study.common.component.responseFieldHandler.IgnoreField;
import lombok.Data;

/**
 * @Author h-bingo
 * @Date 2023-04-24 15:36
 * @Version 1.0
 */
@Data
public class Student {

    @Translate(type = TranslateType.ENUM, enumClass = StatusEnum.class, fillName = "statusName")
    private Integer status;

    private String statusName;

    @Translate(type = TranslateType.ENUM, enumClass = SexEnum.class)
    private String sex;

    @Translate(type = TranslateType.ENUM, enumClass = IgnoreField.class, fillName = "ignoreName")
    private String ignore;
    private String ignoreName;
}
