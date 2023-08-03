package com.bingo.study.common.core.interfaces;

/**
 * 所有域模型都要实现或间接实现
 *
 * @author bingo
 * @date 2022-03-25 13:19
 */
public interface IBaseModel {

    // private String fdId;                 // 主键

    String getFdId();

    void setFdId(String fdId);

    // Class<?> getModelFromClass();
}
