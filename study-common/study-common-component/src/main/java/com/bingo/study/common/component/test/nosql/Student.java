package com.bingo.study.common.component.test.nosql;

import com.bingo.study.common.component.nosql.annotation.NoSql;
import com.bingo.study.common.core.model.BaseModel;
import com.bingo.study.common.es.constant.ElasticSearchConstant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author h-bingo
 * @Date 2023-01-11 11:11
 * @Version 1.0
 */
@Document("xstudent")
@Setter
@Getter
@NoSql(index = ElasticSearchConstant.INDEX_STUDENT, type = ElasticSearchConstant.TYPE_STUDENT)
public class Student extends BaseModel {

    private String name;
    private Integer age;
    private String no;
}
