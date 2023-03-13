package com.bingo.study.common.component.nosql.service;

import com.bingo.study.common.component.nosql.util.NoSqlUtil;
import com.bingo.study.common.component.nosql.wrapper.NoSqlWrapper;
import com.bingo.study.common.core.interfaces.IBaseModel;
import com.bingo.study.common.es.service.ElasticSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

/**
 * @Author h-bingo
 * @Date 2023-01-10 17:37
 * @Version 1.0
 */
@Slf4j
public class EsUpdateService implements NoSqlService {

    private ElasticSearchService elasticSearchService;

    public void setElasticSearchService(ElasticSearchService elasticSearchService) {
        this.elasticSearchService = elasticSearchService;
    }

    @Override
    public boolean update(IBaseModel model) {
        checkNull();

        NoSqlWrapper wrapper = NoSqlUtil.build(model);

        try {
            if (elasticSearchService.isExistsDocument(wrapper.getIndex(), wrapper.getType(), wrapper.getModel().getFdId())) {
                return elasticSearchService.updateDocument(wrapper.getIndex(), wrapper.getType(), wrapper.getModel());
            } else {
                return elasticSearchService.addDocument(wrapper.getIndex(), wrapper.getType(), wrapper.getModel());
            }
        } catch (Exception e) {
            log.warn("ElasticSearch cache update fail", e);
            return false;
        }
    }

    @Override
    public boolean delete(IBaseModel model) {
        checkNull();

        NoSqlWrapper wrapper = NoSqlUtil.build(model);

        try {
            return elasticSearchService.deleteDocument(wrapper.getIndex(), wrapper.getType(), wrapper.getModel().getFdId());
        } catch (Exception e) {
            log.warn("ElasticSearch cache delete fail", e);
            return false;
        }
    }

    private void checkNull() {
        Assert.notNull(elasticSearchService, "elasticSearchService is not inject");
    }
}
