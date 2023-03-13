package com.bingo.study.common.core.utils;

import java.util.*;

/**
 * 集合工具类
 *
 * @author bingo
 * @date 2022-03-23 17:54
 */
public class ArrayUtil {
    /**
     * 判断两个列表中的内容是否一样（忽略顺序）
     *
     * @param list1
     * @param list2
     * @return
     */
    public static <T> boolean isListSame(List<T> list1, List<T> list2) {
        return list1.size() == list2.size() && list1.containsAll(list2);
    }

    /**
     * 判断两个列表中是否有交集
     *
     * @param list1
     * @param list2
     * @return
     */
    public static <T> boolean isListIntersect(List<T> list1, List<T> list2) {
        for (int i = 0; i < list1.size(); i++) {
            if (list2.contains(list1.get(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断两个数组的内容是否一样（忽略顺序）
     *
     * @param objArr1
     * @param objArr2
     * @return
     */
    public static boolean isArraySame(Object[] objArr1, Object[] objArr2) {
        return isListSame(Arrays.asList(objArr1), Arrays.asList(objArr2));
    }

    /**
     * 判断objArr1是否包含了objArr2
     *
     * @param objArr1
     * @param objArr2
     * @return
     */
    public static boolean isArrayContains(Object[] objArr1, Object[] objArr2) {
        return Arrays.asList(objArr1).containsAll(Arrays.asList(objArr2));
    }

    /**
     * 判断两个数组中是否有交集
     *
     * @param objArr1
     * @param objArr2
     * @return
     */
    public static boolean isArrayIntersect(Object[] objArr1, Object[] objArr2) {
        return isListIntersect(Arrays.asList(objArr1), Arrays.asList(objArr2));
    }

    /**
     * 将fromList中的元素添加到toList中，过滤重复值
     *
     * @param fromList
     * @param toList
     */
    public static <T> void concatTwoList(List<T> fromList, List<T> toList) {
        if (fromList == null || toList == null) {
            return;
        }
        for (int i = 0; i < fromList.size(); i++) {
            T obj = fromList.get(i);
            if (!toList.contains(obj)) {
                toList.add(obj);
            }
        }
    }

    /**
     * 判断集合是否为null或empty
     *
     * @param collection
     * @return
     */
    public static <T> boolean isEmpty(Collection<T> collection) {
        if (collection == null) {
            return true;
        }
        return collection.isEmpty();
    }

    /**
     * 判断list是否为null或empty
     *
     * @param list
     * @return
     */
    public static <T> boolean isEmpty(List<T> list) {
        if (list == null) {
            return true;
        }
        return list.isEmpty();
    }

    /**
     * 数组转为List
     */
    public static <T> List<T> convertArrayToList(T[] objects) {
        List<T> rtnList = new ArrayList();
        for (int i = 0; i < objects.length; i++) {
            rtnList.add(objects[i]);
        }
        return rtnList;
    }

    /**
     * 字符串数组转为字符串，使用指定字符连接数组元素
     *
     * @param arr 字符串数组
     * @param c   连接字符
     * @return 字符串表达式
     */
    public static String concat(String[] arr, char c) {
        String rtnStr = "";
        if (arr != null && arr.length > 0) {
            StringBuilder sbd = new StringBuilder();
            for (String str : arr) {
                sbd.append(c).append(str);
            }
            rtnStr = sbd.substring(1);
        }
        return rtnStr;
    }

    /**
     * 数组去重去null（直接在原数组删除后面的重复值）
     */
    public static void unique(List<?> list) {
        for (int i = 0; i < list.size(); i++) {
            Object vi = list.get(i);
            if (vi == null) {
                list.remove(i);
                i--;
                continue;
            }
            for (int j = list.size() - 1; j > i; j--) {
                Object vj = list.get(j);
                if (ObjectUtil.equals(vi, vj)) {
                    list.remove(j);
                }
            }
        }
    }

    /**
     * 集合根据大小分组
     *
     * @param list
     * @param size
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> groupBySize(List<T> list, int size) {
        List<List<T>> rtn = new ArrayList<>();
        int groupNum;
        if ((list.size() % size) == 0) {
            groupNum = list.size() / size;
        } else {
            groupNum = (list.size() / size) + 1;
        }

        for (int i = 0; i < groupNum; i++) {
            if (i == (groupNum - 1)) {
                rtn.add(list.subList(i * size, list.size()));
            } else {
                rtn.add(list.subList(i * size, (i + 1) * size));
            }
        }
        return rtn;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> asList(T[] arr) {
        if (arr == null || arr.length == 0) {
            return Collections.EMPTY_LIST;
        } else {
            return Arrays.asList(arr);
        }
    }
}
