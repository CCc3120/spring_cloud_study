package com.bingo.test.esAndMongo;

import com.bingo.study.common.component.nosql.annotation.NoSql;
import com.bingo.study.common.component.nosql.util.UpdateType;
import com.bingo.study.common.core.model.BaseModel;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @Author h-bingo
 * @Date 2023-09-08 14:56
 * @Version 1.0
 */
@Data
@Document(collection = "esAndMongoModel")
@NoSql(index = "index_es_and_mongo_model", type = "type_es_and_mongo_model", collection = "esAndMongoModel",
        updateType = UpdateType.ALL)
public class EsAndMongoModel extends BaseModel {

    private String fdChar;

    private Integer fdInt;

    private Long fdLong;

    private Date fdDate;
}
