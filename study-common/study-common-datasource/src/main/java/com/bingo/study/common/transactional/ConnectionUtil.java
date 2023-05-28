package com.bingo.study.common.transactional;

import com.bingo.study.common.transactional.holder.*;
import org.springframework.transaction.annotation.Isolation;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Author h-bingo
 * @Date 2023-05-10 15:18
 * @Version 1.0
 */
public class ConnectionUtil {

    public static Connection connectionWrapper(Connection connection) throws SQLException {
        if (!TransactionStatusHolder.isOpen()) {
            return connection;
        }

        DynamicConnection dynamicConnection = new DynamicConnection(connection);
        dynamicConnection.setAutoCommit(false);
        DynamicParamWrapper wrapper = TransactionParamStackHolder.getLatestTransactionParam();
        if (wrapper.getIsolation() != Isolation.DEFAULT) {
            // 设置事物隔离级别
            dynamicConnection.setTransactionIsolation(wrapper.getIsolation().value());
        }
        // 是否只读
        dynamicConnection.setReadOnly(wrapper.isReadOnly());
        // 数据源和事物id绑定
        DynamicConnectionGroupHolder.setDynamicConnection(TransactionGroupHolder.getLatestGroupId(),
                dynamicConnection);
        return dynamicConnection;
    }
}
