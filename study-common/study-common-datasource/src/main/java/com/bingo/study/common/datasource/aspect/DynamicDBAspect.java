package com.bingo.study.common.datasource.aspect;

import com.bingo.study.common.datasource.DynamicDBContextHolder;
import com.bingo.study.common.datasource.annotation.DynamicDB;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;

/**
 * 该代理类必须要在事物前执行
 *
 * @Author h-bingo
 * @Date 2023-04-25 17:34
 * @Version 1.0
 */
@Slf4j
@Order(1) // 在事物之前处理
@Aspect
@ConditionalOnMissingBean({DynamicDBAspect.class})
public class DynamicDBAspect implements InitializingBean {

    /**
     * within 会匹配到标注了指定注解的类，并且在该类的子类中，那些没有重写的父类方法也会被匹配到
     * target 只匹配标注了指定注解的类。不涉及任何其他类
     * annotation 方法级
     */
    @Pointcut("@within(com.bingo.study.common.datasource.annotation.DynamicDB)" +
            "||@annotation(com.bingo.study.common.datasource.annotation.DynamicDB)")
    public void pointcut() {
    }

    @Around("pointcut()&&@annotation(db)")
    public Object doAround(ProceedingJoinPoint joinPoint, DynamicDB db) throws Throwable {
        db = getDynamicDB(joinPoint, db);

        DynamicDBContextHolder.setDataSource(db.value().getCode());

        log.info("切换到数据源: {}", db.value());
        try {
            return joinPoint.proceed();
        } finally {
            DynamicDBContextHolder.cleanDataSource();
        }
    }

    private DynamicDB getDynamicDB(ProceedingJoinPoint joinPoint, DynamicDB db) {
        if (db == null) {
            db = AnnotationUtils.findAnnotation(joinPoint.getTarget().getClass(), DynamicDB.class);
        }
        return db;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("动态切换数据源功能已启动，请使用 @DynamicDB 注解切换数据源");
    }
}
