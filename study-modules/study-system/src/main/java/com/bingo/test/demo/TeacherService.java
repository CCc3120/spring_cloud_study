package com.bingo.test.demo;

import com.bingo.study.common.component.nosql.service.AbstractNoSqlUpdate;
import com.bingo.study.common.component.nosql.service.EsUpdateService;
import com.bingo.study.common.component.nosql.service.MongoUpdateService;
import com.bingo.study.common.component.nosql.util.NoSqlUtil;
import com.bingo.study.common.component.nosql.wrapper.NoSqlWrapper;
import com.bingo.study.common.core.utils.JsonMapper;
import com.bingo.study.common.core.utils.SpringUtil;
import com.bingo.study.common.es.service.ElasticSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author h-bingo
 * @Date 2023-01-12 17:15
 * @Version 1.0
 */
@Slf4j
@Service
public class TeacherService extends AbstractNoSqlUpdate {

    @Autowired(required = false)
    private MongoTemplate mongoTemplate;

    @Autowired
    private ElasticSearchService elasticSearchService;

    @Autowired
    private EsUpdateService esUpdateService;

    @Autowired(required = false)
    private MongoUpdateService mongoUpdateService;

    @Autowired
    private SpringUtil springUtil;


    public void insert(Teacher teacher) {

        super.updateNoSql(teacher);
    }

    public void select(Teacher teacher) throws Exception {
        NoSqlWrapper build = NoSqlUtil.build(teacher);

        Teacher teacher1 = mongoTemplate.findById(teacher.getFdId(), Teacher.class, build.getCollection());
        log.info("mongo:[{}]", JsonMapper.getInstance().toJsonString(teacher1));

        Teacher teacher2 = elasticSearchService.getDocument(build.getIndex(), build.getType(),
                build.getModel().getFdId(), Teacher.class);
        log.info("es:[{}]", JsonMapper.getInstance().toJsonString(teacher2));

    }

    public void delete(Teacher teacher) {

        super.deleteNoSql(teacher);
    }

    public void update(Teacher teacher) {

        super.updateNoSql(teacher);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        this.addNoSqlService(SpringUtil.getBean(EsUpdateService.class));
        try {
            this.addNoSqlService(SpringUtil.getBean(MongoUpdateService.class));
        } catch (BeansException e) {
            System.out.println("MongoUpdateService异常");
        }
    }
}
