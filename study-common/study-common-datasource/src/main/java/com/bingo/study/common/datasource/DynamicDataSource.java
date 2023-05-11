package com.bingo.study.common.datasource;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.bingo.study.common.transactional.ConnectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 多数据源管理
 *
 * @Author h-bingo
 * @Date 2023-04-25 17:27
 * @Version 1.0
 */
// @Component
@Import({DynamicDBProviderImpl.class})
@AutoConfigureBefore({DruidDataSourceAutoConfigure.class})
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Autowired
    private DynamicDBProvider dynamicDBProvider;

    @Override
    public Connection getConnection() throws SQLException {
        return ConnectionUtil.connectionWrapper(super.getConnection());
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return ConnectionUtil.connectionWrapper(super.getConnection(username, password));
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDBContextHolder.getDataSource();
    }

    @Override
    public void afterPropertiesSet() {
        Map<String, DataSource> dataSourceMap = dynamicDBProvider.loadDataSources();
        Map<Object, Object> stringDataSourceMap = new HashMap<>(dataSourceMap);
        super.setDefaultTargetDataSource(stringDataSourceMap.get(DynamicDBType.MASTER.getCode()));
        super.setTargetDataSources(stringDataSourceMap);
        super.afterPropertiesSet();
    }
}
