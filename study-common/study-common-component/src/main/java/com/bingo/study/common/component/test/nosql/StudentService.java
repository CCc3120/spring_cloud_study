package com.bingo.study.common.component.test.nosql;

import com.bingo.study.common.component.nosql.service.AbstractNoSqlUpdate;
import com.bingo.study.common.component.nosql.service.EsUpdateService;
import com.bingo.study.common.component.nosql.service.MongoUpdateService;
import com.bingo.study.common.core.interfaces.IBaseModel;
import com.bingo.study.common.core.utils.SpringUtil;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author h-bingo
 * @Date 2023-01-10 17:34
 * @Version 1.0
 */
// @Service
public class StudentService extends AbstractNoSqlUpdate implements InitializingBean {

    public void update(IBaseModel model) {

        super.updateNoSql(model);
    }

    public void delete(IBaseModel model) {

        super.deleteNoSql(model);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.addNoSqlService(SpringUtil.getBean(EsUpdateService.class));
        super.addNoSqlService(SpringUtil.getBean(MongoUpdateService.class));
    }
}
