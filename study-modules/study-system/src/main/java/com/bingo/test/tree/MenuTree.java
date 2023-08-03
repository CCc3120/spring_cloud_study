package com.bingo.test.tree;

import com.bingo.study.common.core.interfaces.IBaseTreeModel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @Author h-bingo
 * @Date 2023-08-01 15:45
 * @Version 1.0
 */
@AllArgsConstructor
@Data
public class MenuTree implements IBaseTreeModel {

    private String fdName;

    private String fdId;

    private String fdParentId;

    private List<IBaseTreeModel> fdChildList;
}
