package com.bingo.test.demo;

import cn.hutool.extra.spring.SpringUtil;
import com.bingo.study.common.component.lock.LockType;
import com.bingo.study.common.component.lock.annotation.LockKey;
import com.bingo.study.common.component.lock.annotation.RedisLock;
import com.bingo.study.common.component.nosql.service.AbstractNoSqlUpdate;
import com.bingo.study.common.component.nosql.service.EsUpdateService;
import com.bingo.study.common.component.nosql.service.MongoUpdateService;
import com.bingo.study.common.component.nosql.util.NoSqlUtil;
import com.bingo.study.common.component.nosql.wrapper.NoSqlWrapper;
import com.bingo.study.common.core.utils.JsonMapper;
import com.bingo.study.common.datasource.DynamicDBType;
import com.bingo.study.common.datasource.annotation.DynamicDB;
import com.bingo.study.common.es.service.ElasticSearchService;
import com.bingo.study.common.transactional.annotation.DynamicTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired(required = false)
    private ElasticSearchService elasticSearchService;

    // @Autowired
    private JdbcTemplate jdbcTemplate;

    // @Autowired
    // private EsUpdateService esUpdateService;
    //
    @Autowired(required = false)
    private MongoUpdateService mongoUpdateService;
    //
    // @Autowired
    // private SpringUtil springUtil;

    @RedisLock(singleton = true, lockType = LockType.AUTO_RENEWAL_MUTEX, keyPrefix = "testLock")
    public String testLock(Teacher teacher, @LockKey String fdId) throws InterruptedException {
        log.info(Thread.currentThread().getName() + "===执行中");
        Thread.sleep(3 * 1000);
        return "testLock";
    }

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

    @Transactional
    public void demo() {

    }

    public void delete(Teacher teacher) {

        super.deleteNoSql(teacher);
    }

    public void update(Teacher teacher) {

        super.updateNoSql(teacher);
    }

    public void exceSql() {
        exceMaster();
        exceSlave();
        System.out.println("exceSql end");
    }

    @DynamicTransaction
    public void exceSqlTransactional() {
        // System.out.println("exceSqlTransactional:");
        SpringUtil.getBean(TeacherService.class).exceSlave();
        SpringUtil.getBean(TeacherService.class).exceMaster();
        // System.out.println("exceSqlTransactional end");
    }

    // @Transactional(rollbackFor = Exception.class)
    @DynamicDB(value = DynamicDBType.MASTER)
    // @Transactional
    public void exceMaster() {
        System.out.println("exceMaster:");

        String sql = "insert into student (fd_id) values ('1111')";
        int aLong = jdbcTemplate.update(sql);

        // String sql = "select count(*) from student";
        //  sql = "insert into student (fd_id) values ('2222')";
        //  aLong = jdbcTemplate.update(sql);
        // Long aLong = jdbcTemplate.queryForObject(sql, Long.class);
        System.out.println("student count : " + aLong);
    }

    // @Transactional(rollbackFor = Exception.class)
    @DynamicDB(value = DynamicDBType.SLAVE)
    // @Transactional
    public void exceSlave() {
        System.out.println("exceSlave:");

        // String sql = "select count(*) from report_regex_config";
        String sql = "insert into report_regex_config (modality_code, regex_text, replace_text, `order`) values ('2'," +
                "'2','2', 2)";
        int aLong = jdbcTemplate.update(sql);
        // Long aLong = jdbcTemplate.queryForObject(sql, Long.class);
        System.out.println("report_regex_config count : " + aLong);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        this.addNoSqlService(SpringUtil.getBean(EsUpdateService.class));
        this.addNoSqlService(SpringUtil.getBean(MongoUpdateService.class));
        // try {
        //     this.addNoSqlService(SpringUtil.getBean(MongoUpdateService.class));
        // } catch (BeansException e) {
        //     System.out.println("MongoUpdateService异常");
        // }
    }
}
