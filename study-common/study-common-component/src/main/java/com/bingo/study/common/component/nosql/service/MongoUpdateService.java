package com.bingo.study.common.component.nosql.service;

import com.bingo.study.common.component.nosql.util.NoSqlUtil;
import com.bingo.study.common.component.nosql.wrapper.NoSqlWrapper;
import com.bingo.study.common.core.interfaces.IBaseModel;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.Assert;

/**
 * @Author h-bingo
 * @Date 2023-01-10 17:37
 * @Version 1.0
 */
@Slf4j
public class MongoUpdateService implements NoSqlService {

    private MongoTemplate mongoTemplate;

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public boolean update(IBaseModel model) {
        checkNull();

        NoSqlWrapper wrapper = NoSqlUtil.build(model);

        Query query = new Query(Criteria.where("_id").is(wrapper.getModel().getFdId()));
        if (mongoTemplate.count(query, wrapper.getCollection()) > 0) {
            Update update = new Update();

            NoSqlUtil.buildUpdate(update, wrapper);

            UpdateResult updateResult = mongoTemplate.updateFirst(query, update, wrapper.getCollection());
            return updateResult.wasAcknowledged();
        } else {
            mongoTemplate.insert(wrapper.getModel(), wrapper.getCollection());
        }
        return true;
    }

    @Override
    public boolean delete(IBaseModel model) {
        checkNull();

        NoSqlWrapper wrapper = NoSqlUtil.build(model);

        Query query = new Query(Criteria.where("_id").is(wrapper.getModel().getFdId()));
        DeleteResult deleteResult = mongoTemplate.remove(query, wrapper.getCollection());
        return deleteResult.wasAcknowledged();
    }

    private void checkNull() {
        Assert.notNull(mongoTemplate, "mongoTemplate is not inject");
    }
}
