package com.bingo.test.mainTest;

/**
 * @Author h-bingo
 * @Date 2023-06-08 15:03
 * @Version 1.0
 */
public class TestMainClass {
    public static void main(String[] args) {



        // try {
        //     int a = 1 / 0;
        // } catch (Exception e) {
        //     e.printStackTrace();
        //
        //     String s = ExceptionUtil.stacktraceToString(e);
        //
        //     System.out.println(s);
        //
        //     String s1 = ExceptionUtil.stacktraceToOneLineString(e);
        //
        //     System.out.println(s1);
        //
        //     String stackTrace = ExceptionUtils.getStackTrace(e);
        //
        //     System.out.println(stackTrace);
        //
        //     String stackTrace1 = org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e);
        //
        //
        //     System.out.println(stackTrace1);
        //
        // }



        // Object o1, o2 = o1 = new Object();

        // System.out.println(o1.hashCode());
        // System.out.println(o2.hashCode());
        // o1 = new Object();

        // System.out.println(o1.hashCode());
        // System.out.println(o2.hashCode());

        // Object o1 = new Object();
        //
        // Object o2 = o1;
        //
        // System.out.println(o1.hashCode());
        // System.out.println(o2.hashCode());
        //
        // o1 = new Object();
        //
        // System.out.println(o1.hashCode());
        // System.out.println(o2.hashCode());
        //
        //
        // o2 = o1 = new Object();
        //
        // System.out.println(o1.hashCode());
        // System.out.println(o2.hashCode());

        // HashMap<String, String> map = new HashMap<>();
        //
        // map.put("a", "123");
        // map.put("q", "1234");
        // map.put("q", "12345");
        // // String q = map.putIfAbsent("q", "1234");
        //
        // System.out.println(map);
        // int hashValue = (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);

        // char c = 'a' + 16;
        //
        // System.out.println(c);
        // // a
        // Object key = "q";
        //
        // hashValue(key);
        //
        // int a = 60 + 1024;
        // int b = 1023;
        // int c = a & b;
        //
        // System.out.println(c);
        //
        // Map<String, String> map = new HashMap<>();
        // System.out.println(32 & 16);

        // System.out.println( 15 & 20);
        // System.out.println( 31 & 20);

        // System.out.println(9 & 6);
        // System.out.println(9 & 7);
        // System.out.println(9 & 8);
        // System.out.println(9 & 9);
        // System.out.println(9 & 10);
        // System.out.println(9 & 11);
        // System.out.println(9 & 12);


        // double d = 0.1538;
        //
        // System.out.println(d * 100);
        //     id     parentId    name
        //     0        0          qqq
        //     1        0          www
        //     2        1          sss
        //     3        2          aaa

        //     1-张三 2-李四 4-王五 8 赵六
        //     选中 李四和王五  => 结果是 2 ｜ 4 = 6 （数据库存6）
        //     取值时，循环所有项 6 & 2 > 0 即选中

        // Date date = ((((((((((new Date()))))))))));
        // System.out.println(date);

        //     userId     deptName    deptId
        //     1            aa          22
        //     2            aa          22
        //     3            aa          22
        //     4            aa          22
        //     5            aa          22


        // 合并后的数据
        //     1,2,3,4,5    aa           22

    }

    public static void hashValue(Object key) {
        int size = 16;

        int h = key.hashCode();

        int hashValue = 0;

        if (key != null) {
            int a = h >>> 16;
            hashValue = h ^ a;
        }


        int n = size; // map size
        int i = (n - 1) & hashValue;
        System.out.println(hashValue);
        System.out.println(i);
    }
}
