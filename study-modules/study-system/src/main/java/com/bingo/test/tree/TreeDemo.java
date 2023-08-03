package com.bingo.test.tree;

import com.bingo.study.common.core.utils.TreeUtil;
import com.google.common.base.Stopwatch;

import java.util.*;

/**
 * @Author h-bingo
 * @Date 2023-08-01 15:46
 * @Version 1.0
 */
public class TreeDemo {
    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 10; i++) {
            group();

            down();

            up();

            System.out.println("==========");
            System.gc();
            Thread.sleep(1000);
        }
    }

    public static List<MenuTree> radom() {
        Set<MenuTree> set = new HashSet<>();
        String[] id = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",};
        String[] id2 = {"00", "10", "20", "30", "40", "50", "60", "70", "80", "90",};
        String[] id3 = {"000", "100", "200", "300", "400", "500", "600", "700", "800", "900",};
        Random random = new Random();
        for (int i = 0; i < 100000; i++) {
            set.add(new MenuTree("", id[random.nextInt(10)], "", null));
            set.add(new MenuTree("", id2[random.nextInt(10)], id[random.nextInt(10)], null));
            set.add(new MenuTree("", id3[random.nextInt(10)], id2[random.nextInt(10)], null));
        }

        return new ArrayList<>(set);
    }

    public static void up() {
        // MenuTree t10 = new MenuTree("一级0", "10", "", null);
        // MenuTree t11 = new MenuTree("一级1", "11", "", null);
        // MenuTree t12 = new MenuTree("一级2", "12", "", null);
        // MenuTree t13 = new MenuTree("一级3", "13", "", null);
        // MenuTree t14 = new MenuTree("一级4", "14", "", null);
        //
        // MenuTree t20 = new MenuTree("二级0", "20", "10", null);
        // MenuTree t21 = new MenuTree("二级1", "21", "10", null);
        // MenuTree t22 = new MenuTree("二级2", "22", "10", null);
        // MenuTree t23 = new MenuTree("二级3", "23", "10", null);
        // MenuTree t24 = new MenuTree("二级4", "24", "10", null);
        //
        //
        // MenuTree t30 = new MenuTree("三级0", "30", "20", null);
        // MenuTree t31 = new MenuTree("三级1", "31", "20", null);
        // MenuTree t32 = new MenuTree("三级2", "32", "20", null);
        // MenuTree t33 = new MenuTree("三级3", "33", "20", null);
        // MenuTree t34 = new MenuTree("三级4", "34", "20", null);
        //
        // List<MenuTree> list = new ArrayList<>();
        //
        // list.add(t22);
        // list.add(t32);
        // list.add(t12);
        //
        // list.add(t20);
        // list.add(t10);
        // list.add(t30);
        //
        // list.add(t13);
        // list.add(t23);
        // list.add(t33);
        //
        // list.add(t24);
        // list.add(t34);
        // list.add(t14);
        //
        //
        // list.add(t21);
        // list.add(t11);
        // list.add(t31);


        Stopwatch stopwatch = Stopwatch.createStarted();

        List<MenuTree> modelList = TreeUtil.buildTreeToUp(radom(), true, Comparator.comparing(MenuTree::getFdId));

        stopwatch.stop();
        System.out.println("up: " + stopwatch);
    }

    public static void down() {
        // MenuTree t10 = new MenuTree("一级0", "10", "", null);
        // MenuTree t11 = new MenuTree("一级1", "11", "", null);
        // MenuTree t12 = new MenuTree("一级2", "12", "", null);
        // MenuTree t13 = new MenuTree("一级3", "13", "", null);
        // MenuTree t14 = new MenuTree("一级4", "14", "", null);
        //
        // MenuTree t20 = new MenuTree("二级0", "20", "10", null);
        // MenuTree t21 = new MenuTree("二级1", "21", "10", null);
        // MenuTree t22 = new MenuTree("二级2", "22", "10", null);
        // MenuTree t23 = new MenuTree("二级3", "23", "10", null);
        // MenuTree t24 = new MenuTree("二级4", "24", "10", null);
        //
        //
        // MenuTree t30 = new MenuTree("三级0", "30", "20", null);
        // MenuTree t31 = new MenuTree("三级1", "31", "20", null);
        // MenuTree t32 = new MenuTree("三级2", "32", "20", null);
        // MenuTree t33 = new MenuTree("三级3", "33", "20", null);
        // MenuTree t34 = new MenuTree("三级4", "34", "20", null);
        //
        // List<MenuTree> list = new ArrayList<>();
        //
        // list.add(t22);
        // list.add(t32);
        // list.add(t12);
        //
        // list.add(t20);
        // list.add(t10);
        // list.add(t30);
        //
        // list.add(t13);
        // list.add(t23);
        // list.add(t33);
        //
        // list.add(t24);
        // list.add(t34);
        // list.add(t14);
        //
        //
        // list.add(t21);
        // list.add(t11);
        // list.add(t31);


        Stopwatch stopwatch = Stopwatch.createStarted();

        List<MenuTree> modelList = TreeUtil.buildTreeToDown(radom(), true, Comparator.comparing(MenuTree::getFdId));

        stopwatch.stop();
        System.out.println("down: " + stopwatch);
    }

    public static void group() {
        // MenuTree t10 = new MenuTree("一级0", "10", "", null);
        // MenuTree t11 = new MenuTree("一级1", "11", "", null);
        // MenuTree t12 = new MenuTree("一级2", "12", "", null);
        // MenuTree t13 = new MenuTree("一级3", "13", "", null);
        // MenuTree t14 = new MenuTree("一级4", "14", "", null);
        //
        // MenuTree t20 = new MenuTree("二级0", "20", "10", null);
        // MenuTree t21 = new MenuTree("二级1", "21", "10", null);
        // MenuTree t22 = new MenuTree("二级2", "22", "10", null);
        // MenuTree t23 = new MenuTree("二级3", "23", "10", null);
        // MenuTree t24 = new MenuTree("二级4", "24", "10", null);
        //
        //
        // MenuTree t30 = new MenuTree("三级0", "30", "20", null);
        // MenuTree t31 = new MenuTree("三级1", "31", "20", null);
        // MenuTree t32 = new MenuTree("三级2", "32", "20", null);
        // MenuTree t33 = new MenuTree("三级3", "33", "20", null);
        // MenuTree t34 = new MenuTree("三级4", "34", "20", null);
        //
        // List<MenuTree> list = new ArrayList<>();
        //
        // list.add(t22);
        // list.add(t32);
        // list.add(t12);
        //
        // list.add(t20);
        // list.add(t10);
        // list.add(t30);
        //
        // list.add(t13);
        // list.add(t23);
        // list.add(t33);
        //
        // list.add(t24);
        // list.add(t34);
        // list.add(t14);
        //
        //
        // list.add(t21);
        // list.add(t11);
        // list.add(t31);


        Stopwatch stopwatch = Stopwatch.createStarted();

        List<MenuTree> modelList = TreeUtil.buildTreeToGroup(radom(), true, Comparator.comparing(MenuTree::getFdId));

        stopwatch.stop();
        System.out.println("group: " + stopwatch);
    }
}
