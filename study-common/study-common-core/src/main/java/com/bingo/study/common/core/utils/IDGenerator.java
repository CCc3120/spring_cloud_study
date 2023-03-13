package com.bingo.study.common.core.utils;

import java.util.Date;
import java.util.UUID;

/**
 * @author bingo
 * @date 2022-03-24 16:31
 */
public class IDGenerator {

    /**
     * 生成主键（32位）
     *
     * @return
     */
    public static String generateID() {
        return generateID(System.currentTimeMillis());
    }

    /**
     * 根据指定时间生成主键，该方法只能用来对比主键生成时间，切忌不能用来生成主键插入数据库
     * <p>
     *
     * @param date 时间
     * @return
     */
    public static String generateID(Date date) {
        return generateID(date.getTime());
    }

    /**
     * 根据指定时间生成主键
     *
     * @param time
     * @return
     */
    public static String generateID(long time) {
        String rtnVal = Long.toHexString(time);
        rtnVal += UUID.randomUUID();
        rtnVal = rtnVal.replaceAll("-", "");
        return rtnVal.substring(0, 32);
    }

    /**
     * 根据ID获取该ID创建的时间
     *
     * @param id
     * @return
     */
    public static Date getIDCreateTime(String id) {
        String timeInfo = id.substring(0, 11);
        return new Date(Long.parseLong(timeInfo, 16));
    }
}
