package com.bingo.test.tree;

import com.bingo.study.common.core.utils.JsonMapper;
import com.bingo.study.common.core.utils.TreeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author h-bingo
 * @Date 2023-08-01 15:46
 * @Version 1.0
 */
public class TreeDemo {
    public static void main(String[] args) {
        MenuTree t10 = new MenuTree("一级0", "10", "", null);
        MenuTree t11 = new MenuTree("一级1", "11", "", null);
        MenuTree t12 = new MenuTree("一级2", "12", "", null);
        MenuTree t13 = new MenuTree("一级3", "13", "", null);
        MenuTree t14 = new MenuTree("一级4", "14", "", null);

        MenuTree t20 = new MenuTree("二级0", "20", "10", null);
        MenuTree t21 = new MenuTree("二级1", "21", "10", null);
        MenuTree t22 = new MenuTree("二级2", "22", "10", null);
        MenuTree t23 = new MenuTree("二级3", "23", "10", null);
        MenuTree t24 = new MenuTree("二级4", "24", "10", null);


        MenuTree t30 = new MenuTree("三级0", "30", "20", null);
        MenuTree t31 = new MenuTree("三级1", "31", "20", null);
        MenuTree t32 = new MenuTree("三级2", "32", "20", null);
        MenuTree t33 = new MenuTree("三级3", "33", "20", null);
        MenuTree t34 = new MenuTree("三级4", "34", "20", null);

        List<MenuTree> list = new ArrayList<>();

        list.add(t22);
        list.add(t32);
        list.add(t12);

        list.add(t20);
        list.add(t10);
        list.add(t30);

        list.add(t13);
        list.add(t23);
        list.add(t33);

        list.add(t24);
        list.add(t34);
        list.add(t14);


        list.add(t21);
        list.add(t11);
        list.add(t31);

        // List<MenuTree> modelList = TreeUtil.buildTreeToGroup(list, true, Comparator.comparing(MenuTree::getFdId));

        // System.out.println(JsonMapper.getInstance().toJsonString(modelList));

        List<MenuTree> treeToUp = TreeUtil.buildTreeToUp(list);

        System.out.println(JsonMapper.getInstance().toJsonString(treeToUp));
        // LinkedHashMap linkedHashMap = new LinkedHashMap();
        // linkedHashMap.put();
    }
}
