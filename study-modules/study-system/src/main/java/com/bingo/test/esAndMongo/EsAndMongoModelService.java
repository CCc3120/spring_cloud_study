package com.bingo.test.esAndMongo;

import com.bingo.study.common.component.nosql.service.AbstractNoSqlUpdate;
import com.bingo.study.common.component.nosql.service.EsUpdateService;
import com.bingo.study.common.component.nosql.service.MongoUpdateService;
import com.bingo.study.common.core.utils.SpringUtil;
import com.bingo.study.common.core.web.page.PageResult;
import com.bingo.study.common.core.web.request.BaseSearchModel;
import com.bingo.study.common.es.service.ElasticSearchService;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author h-bingo
 * @Date 2023-09-08 15:03
 * @Version 1.0
 */
@Service
public class EsAndMongoModelService extends AbstractNoSqlUpdate {

    @Autowired(required = false)
    private ElasticSearchService elasticSearchService;
    @Autowired(required = false)
    private MongoTemplate mongoTemplate;

    public void insert(EsAndMongoModel model) {
        this.updateNoSql(model);
    }

    public void delete(EsAndMongoModel model) {
        this.deleteNoSql(model);
    }

    public List<EsAndMongoModel> listEs() throws Exception {
        BaseSearchModel model = new BaseSearchModel();
        model.setPageNum(1L);
        model.setPageSize(5000L);

        PageResult<EsAndMongoModel> page = elasticSearchService.searchRequest(EsAndMongoModel.class,
                "index_es_and_mongo_model", model, boolQueryBuilder -> {
                    // boolQueryBuilder.must(QueryBuilders.matchQuery(StringUtil.join("fdChar",
                    //         ElasticSearchConstant.KEYWORD), "测试数据1"));
                    // boolQueryBuilder.must(QueryBuilders.termQuery("fdChar", "测试数据1"));
                    boolQueryBuilder.must(QueryBuilders.matchPhrasePrefixQuery("fdChar", "测试数据1"));
                    // QueryBuilders.matchPhraseQuery() // 短语匹配可用模糊查询
                    // QueryBuilders.matchPhrasePrefixQuery() // 短语前缀匹配，用于模糊查询

                    // BoolQueryBuilder builder = QueryBuilders.boolQuery();
                    // TermQueryBuilder queryBuilder = QueryBuilders.termQuery("fdChar", "测试数据1");
                    // builder.must(queryBuilder);
                    // TermQueryBuilder queryBuilder1 = QueryBuilders.termQuery("fdChar2", "测试数据1");
                    // builder.must(queryBuilder1);
                    //
                    // BoolQueryBuilder should = QueryBuilders.boolQuery();
                    // should.should(QueryBuilders.termQuery("fdChar3", "测试数据1"));
                    // should.should(QueryBuilders.termQuery("fdChar4", "测试数据1"));
                    // builder.must(should);
                    // builder.must(QueryBuilders.termQuery("fdChar", "测试数据1"));

                });

        return page.getDataList();
    }

    private String collection = "esAndMongoModel";

    public List<EsAndMongoModel> listMongo() throws Exception {
        EsAndMongoModel probe = new EsAndMongoModel();
            probe.setFdChar("测试数据1");
        //
        // List<EsAndMongoModel> mongoModels = esAndMongoModelRepository.findAll(Example.of(probe));
        //
        // return mongoModels;
        Query query = new Query();

        // Criteria alike = Criteria.where("fdChar").alike(Example.of(probe)); // 这种方法会报错
        Criteria alike = Criteria.where("fdChar").regex("测试数据1");
        query.addCriteria(alike);

        return mongoTemplate.find(query, EsAndMongoModel.class, collection);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.addNoSqlService(SpringUtil.getBean(EsUpdateService.class));
        this.addNoSqlService(SpringUtil.getBean(MongoUpdateService.class));
    }
}
