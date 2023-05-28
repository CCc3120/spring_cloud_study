package com.bingo.study.common.transactional.aspect;

import com.bingo.study.common.core.utils.IDGenerator;
import com.bingo.study.common.transactional.DynamicParamWrapper;
import com.bingo.study.common.transactional.DynamicTransactionManager;
import com.bingo.study.common.transactional.annotation.DynamicTransaction;
import com.bingo.study.common.transactional.holder.TransactionParamStackHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * @Author h-bingo
 * @Date 2023-04-27 17:51
 * @Version 1.0
 */
@Slf4j
@Aspect
@ConditionalOnMissingBean({DynamicTransactionAspect.class})
public class DynamicTransactionAspect implements InitializingBean {

    @Autowired
    private DynamicTransactionManager dynamicTransactionManager;

    @Pointcut("@within(com.bingo.study.common.transactional.annotation.DynamicTransaction)" +
            "||@annotation(com.bingo.study.common.transactional.annotation.DynamicTransaction)")
    public void dynamicTransaction() {
    }

    @Around("dynamicTransaction()&&@annotation(dt)")
    public Object doAround(ProceedingJoinPoint joinPoint, DynamicTransaction dt) throws Throwable {
        dt = getDynamicTransaction(joinPoint, dt);

        DynamicParamWrapper paramWrapper = getDynamicParamWrapper(dt);

        TransactionParamStackHolder.addTransactionParam(paramWrapper);

        dynamicTransactionManager.begin();

        try {
            // 执行业务
            Object proceed = joinPoint.proceed();
            // 提交事务
            dynamicTransactionManager.commit();
            return proceed;
        } catch (Exception e) {
            dynamicTransactionManager.rollBack();
            throw e;
        } finally {
            TransactionParamStackHolder.getAndRemoveLatestTransactionParam();
            // 清空 事务 连接，关闭当前事务
            dynamicTransactionManager.close(paramWrapper);
        }
    }

    private DynamicTransaction getDynamicTransaction(ProceedingJoinPoint joinPoint, DynamicTransaction dt) {
        if (dt == null) {
            dt = AnnotationUtils.findAnnotation(joinPoint.getTarget().getClass(), DynamicTransaction.class);
        }
        return dt;
    }

    private DynamicParamWrapper getDynamicParamWrapper(DynamicTransaction dt) {
        String uuid = IDGenerator.generateID();
        DynamicParamWrapper wrapper = new DynamicParamWrapper();
        wrapper.setIsolation(dt.isolation());
        wrapper.setReadOnly(dt.readOnly());
        wrapper.setPropagation(dt.propagation());
        wrapper.setTransactionId(uuid);
        return wrapper;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("动态切换数据源事物功能已启动，请使用 @DynamicTransaction 注解配置事物，需要和 @DynamicDB 注解切换数据源配合使用");
    }
}
