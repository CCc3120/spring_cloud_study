package com.bingo.test.demo;


import com.bingo.study.common.component.nosql.annotation.NoSql;
import com.bingo.study.common.component.nosql.util.UpdateType;
import com.bingo.study.common.core.model.BaseModel;
import com.bingo.study.common.es.constant.ElasticSearchConstant;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Author h-bingo
 * @Date 2023-01-12 17:03
 * @Version 1.0
 */
@Getter
@Setter
@NoSql(index = ElasticSearchConstant.INDEX_TEACHER, type = "teacher", collection = "teacher", updateType = UpdateType.NOT_NULL)
public class Teacher extends BaseModel {

    private String fdName;

    private Integer fdAge;

    private Date fdBirthday;

    private String fdNo;

    private String fdIdCard;

    private String fdSex;
}
