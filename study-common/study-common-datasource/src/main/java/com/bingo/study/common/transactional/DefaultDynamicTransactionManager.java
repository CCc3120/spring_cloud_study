package com.bingo.study.common.transactional;

import com.bingo.study.common.transactional.holder.*;
import org.springframework.transaction.annotation.Propagation;

import java.util.List;

/**
 * @Author h-bingo
 * @Date 2023-05-11 16:52
 * @Version 1.0
 */
public class DefaultDynamicTransactionManager implements DynamicTransactionManager {

    @Override
    public void begin() {
        this.begin(TransactionParamStackHolder.getLatestTransactionParam());
    }

    @Override
    public void begin(DynamicParamWrapper wrapper) {
        if (wrapper.getPropagation() == Propagation.REQUIRED) { // 支持当前事物，没有则创建
            if (TransactionStatusHolder.isOpen()) {
                TransactionGroupHolder.addTransaction(null, wrapper);
            } else {
                TransactionStatusHolder.begin();
                TransactionGroupHolder.addTransaction(wrapper.getTransactionId(), wrapper);
            }
        } else if (wrapper.getPropagation() == Propagation.SUPPORTS) { // 支持当前事物，没有也不创建
            if (TransactionStatusHolder.isOpen()) {
                TransactionGroupHolder.addTransaction(null, wrapper);
            }
        } else if (wrapper.getPropagation() == Propagation.MANDATORY) { // 支持当前事物，若当前事物没有则抛异常
            if (TransactionStatusHolder.isOpen()) {
                TransactionGroupHolder.addTransaction(null, wrapper);
            } else {
                throw new RuntimeException("当前事物不存在");
            }
        } else if (wrapper.getPropagation() == Propagation.REQUIRES_NEW) { // 挂起当前事物，创建新事物
            if (!TransactionStatusHolder.isOpen()) {
                TransactionStatusHolder.begin();
            }
            TransactionGroupHolder.addTransaction(wrapper.getTransactionId(), wrapper);
        } else if (wrapper.getPropagation() == Propagation.NOT_SUPPORTED) { // 若存在事物则暂停，以非事物执行

        } else if (wrapper.getPropagation() == Propagation.NEVER) { // 存在事物则抛异常
            if (TransactionStatusHolder.isOpen()) {
                throw new RuntimeException("当前存在事物");
            }
        } else if (wrapper.getPropagation() == Propagation.NESTED) { // 存在事物则嵌套执行
            if (TransactionStatusHolder.isOpen()) {
                TransactionGroupHolder.addTransaction(null, wrapper);
            }
        }
    }

    @Override
    public void commit() throws Exception {
        this.commit(TransactionParamStackHolder.getLatestTransactionParam());
    }

    @Override
    public void commit(DynamicParamWrapper wrapper) throws Exception {
        String latestGroupId = TransactionGroupHolder.getLatestGroupId();
        if (wrapper.getTransactionId().equals(latestGroupId)) { // 判断最近一次是否符合提交要求
            List<DynamicConnection> dynamicConn = DynamicConnectionGroupHolder.getGroupConnection(latestGroupId);
            for (DynamicConnection connection : dynamicConn) {
                connection.commitDynamicDbTran();
                connection.closeDynamicDbTran();
            }
            // 移除当前嵌套记录
            TransactionGroupHolder.getAndRemoveLatestGroupId();
        }
    }

    @Override
    public void rollBack()throws Exception {
        this.rollBack(TransactionParamStackHolder.getLatestTransactionParam());
    }

    @Override
    public void rollBack(DynamicParamWrapper wrapper) throws Exception {
        String latestGroupId = TransactionGroupHolder.getLatestGroupId();
        if (wrapper.getTransactionId().equals(latestGroupId)) { // 判断最近一次是否符合提交要求
            List<DynamicConnection> dynamicConn = DynamicConnectionGroupHolder.getGroupConnection(latestGroupId);
            for (DynamicConnection connection : dynamicConn) {
                connection.rollback();
                connection.closeDynamicDbTran();
            }

            // 移除当前嵌套记录
            TransactionGroupHolder.getAndRemoveLatestGroupId();
        }
    }

    @Override
    public void close() {
        this.close(TransactionParamStackHolder.getLatestTransactionParam());
    }

    @Override
    public void close(DynamicParamWrapper wrapper) {
        if (TransactionGroupHolder.getLatestGroupId() == null) {
            TransactionGroupHolder.clean();
            TransactionParamStackHolder.clean();
            TransactionStatusHolder.clean();
            DynamicConnectionGroupHolder.clean();
        }
    }
}
