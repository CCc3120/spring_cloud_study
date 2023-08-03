package com.bingo.study.common.core.interfaces;

import java.util.List;

/**
 * 树结构基类
 *
 * @Author h-bingo
 * @Date 2023-08-01 15:00
 * @Version 1.0
 */
public interface IBaseTreeModel extends IBaseModel {

    // private String fdParentId;

    String getFdParentId();

    void setFdParentId(String fdParentId);

    // private List<IBaseTreeModel> fdChildList;

    List<IBaseTreeModel> getFdChildList();

    void setFdChildList(List<IBaseTreeModel> fdChildList);
}
