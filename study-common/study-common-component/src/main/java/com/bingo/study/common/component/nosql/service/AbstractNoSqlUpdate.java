package com.bingo.study.common.component.nosql.service;

import com.bingo.study.common.core.interfaces.IBaseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author h-bingo
 * @Date 2023-01-09 17:24
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractNoSqlUpdate implements InitializingBean {

    private final List<NoSqlService> observers = new ArrayList<>();

    protected void addNoSqlService(NoSqlService service) {
        if (!observers.contains(service)) {
            observers.add(service);
        }
    }

    protected void removeNoSqlService(NoSqlService service) {
        observers.remove(service);
    }

    /**
     * 更新/插入相关nosql缓存
     *
     * @Param [model]
     * @Return void
     * @Date 2023-01-11 11:54
     */
    public void updateNoSql(IBaseModel model) {
        for (NoSqlService observer : observers) {
            observer.update(model);
        }
    }

    /**
     * 删除相关mosql缓存
     *
     * @Param [model]
     * @Return void
     * @Date 2023-01-11 11:54
     */
    public void deleteNoSql(IBaseModel model) {
        for (NoSqlService observer : observers) {
            observer.delete(model);
        }
    }
}
