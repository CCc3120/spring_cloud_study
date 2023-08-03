package com.bingo.study.common.core.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.bingo.study.common.core.interfaces.IBaseTreeModel;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author h-bingo
 * @Date 2023-08-01 15:05
 * @Version 1.0
 */
public class TreeUtil {

    /**
     * 构造树结构(分组法)
     *
     * @param treeModelList
     * @return
     */
    public static <T extends IBaseTreeModel> List<T> buildTreeToGroup(List<T> treeModelList) {
        return buildTreeToGroup(treeModelList, false, null);
    }

    /**
     * 构造树结构(分组法)
     *
     * @param treeModelList
     * @param sort
     * @param comparator
     * @param <T>
     * @return
     */
    public static <T extends IBaseTreeModel> List<T> buildTreeToGroup(List<T> treeModelList, boolean sort,
            Comparator<T> comparator) {
        if (sort) {
            treeModelList.sort(comparator);
        }
        // 所有的根节点
        List<T> result =
                treeModelList.stream().filter(t -> StringUtils.isBlank(t.getFdParentId())).collect(Collectors.toList());
        // 根据父节点进行分组
        Map<String, List<IBaseTreeModel>> map =
                treeModelList.stream().collect(Collectors.groupingBy(IBaseTreeModel::getFdParentId));
        // 递归遍历
        recursionFnTree(result, map);
        return result;
    }

    /**
     * 递归查找树子集(分组法)
     *
     * @param treeModelList
     * @param map
     */
    private static <T extends IBaseTreeModel> void recursionFnTree(List<T> treeModelList,
            Map<String, List<IBaseTreeModel>> map) {
        for (IBaseTreeModel treeModel : treeModelList) {
            List<IBaseTreeModel> list = map.get(treeModel.getFdId());
            if (!CollectionUtil.isEmpty(list)) {
                treeModel.setFdChildList(list);
                // 递归查找子集
                recursionFnTree(list, map);
            }
        }
    }

    public static <T extends IBaseTreeModel> List<T> buildTreeToDown(List<T> treeModelList) {
        return null;
    }

    /**
     * 构造树结构(向上查找法)
     *
     * @param treeModelList
     * @param <T>
     * @return
     */
    public static <T extends IBaseTreeModel> List<T> buildTreeToUp(List<T> treeModelList) {
        return buildTreeToUp(treeModelList, false, null);
    }

    /**
     * 构造树结构(向上查找法)
     *
     * @param treeModelList
     * @param sort
     * @param comparator
     * @param <T>
     * @return
     */
    public static <T extends IBaseTreeModel> List<T> buildTreeToUp(List<T> treeModelList, boolean sort,
            Comparator<T> comparator) {
        Map<String, T> map;
        if (sort) {
            treeModelList.sort(comparator);
            map = new LinkedHashMap<>();
        } else {
            map = new HashMap<>();
        }

        // 构造map数据
        treeModelList.forEach(treeModel -> map.put(treeModel.getFdId(), treeModel));
        return recursionFnTree(map);
    }

    /**
     * 递归查找父级(向上查找法)
     *
     * @param map
     * @param <T>
     * @return
     */
    private static <T extends IBaseTreeModel> List<T> recursionFnTree(Map<String, T> map) {
        List<String> remove = new ArrayList<>();

        Collection<T> values = map.values();
        for (T value : values) {
            T t = map.get(value.getFdParentId());
            if (t != null && !t.getFdId().equals(value.getFdId())) {
                List<IBaseTreeModel> fdChildList = t.getFdChildList();
                if (fdChildList == null) {
                    fdChildList = new ArrayList<>();
                    t.setFdChildList(fdChildList);
                }
                fdChildList.add(value);

                remove.add(value.getFdId());
            }
        }

        remove.forEach(map::remove);

        if (!remove.isEmpty()) {
            recursionFnTree(map);
        }
        return new ArrayList<>(map.values());
    }
}
