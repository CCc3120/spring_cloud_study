package com.bingo.study.common.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具
 *
 * @author bingo
 * @date 2022-03-24 16:59
 */
public class DateUtil {

    public static final long SECOND = 1000;

    public static final long MINUTE = SECOND * 60;

    public static final long HOUR = MINUTE * 60;

    public static final long DAY = HOUR * 24;

    public static final long WEEK = DAY * 7;

    public static final long MONTH = DAY * 30;

    public static final long YEAR = DAY * 365;

    public static String PATTERN_YYYY_MM = "yyyy-MM";

    public static String PATTERN_YYYY_MM_DD = "yyyy-MM-dd";

    public static String PATTERN_HH_MM = "HH:mm";

    public static String PATTERN_HH_MM_SS = "HH:mm:ss";

    public static String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";

    public static String DEFAULT_PATTERN = PATTERN_DATETIME;

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 字符串转日期
     *
     * @param strDate
     * @return
     */
    public static Date convertStringToDate(String strDate) {
        return convertStringToDate(strDate, null);
    }

    /**
     * 字符串转日期
     *
     * @param strDate
     * @param pattern
     * @return
     */
    public static Date convertStringToDate(String strDate, String pattern) {
        if (StringUtil.isNull(strDate)) {
            return null;
        }
        if (StringUtil.isNull(pattern)) {
            pattern = DEFAULT_PATTERN;
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(strDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期转字符串
     *
     * @param date
     * @return
     */
    public static String convertDateToString(Date date) {
        return convertDateToString(date, null);
    }

    /**
     * 日期转字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String convertDateToString(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        if (StringUtil.isNull(pattern)) {
            pattern = DEFAULT_PATTERN;
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 日开始时间
     *
     * @param date
     * @return
     */
    public static Date getDayStartTime(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = getCalendar(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 日结束时间
     *
     * @param date
     * @return
     */
    public static Date getDayEndTime(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = getCalendar(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 月开始时间
     *
     * @param date
     * @return
     */
    public static Date getMonthStartTime(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = getCalendar(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return getDayStartTime(calendar.getTime());
    }

    /**
     * 月结束时间
     *
     * @param date
     * @return
     */
    public static Date getMonthEndTime(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = getCalendar(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return getDayEndTime(calendar.getTime());
    }

    /**
     * 年开始时间
     *
     * @param date
     * @return
     */
    public static Date getYearStartTime(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = getCalendar(date);
        calendar.set(Calendar.MONTH, 0);
        return getMonthStartTime(calendar.getTime());
    }

    /**
     * 年结束时间
     *
     * @param date
     * @return
     */
    public static Date getYearEndTime(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = getCalendar(date);
        calendar.set(Calendar.MONTH, 11);
        return getMonthEndTime(calendar.getTime());
    }

    /**
     * 日期计算
     *
     * @param start
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static Date calDate(Date start, Integer year, Integer month, Integer day, Integer hour,
            Integer minute, Integer second) {
        if (start == null) {
            return null;
        }

        Calendar calendar = getCalendar(start);
        if (year != null) {
            calendar.add(Calendar.YEAR, year);
        }
        if (month != null) {
            calendar.add(Calendar.MONTH, month);
        }
        if (day != null) {
            calendar.add(Calendar.DAY_OF_YEAR, day);
        }
        if (hour != null) {
            calendar.add(Calendar.HOUR, hour);
        }
        if (minute != null) {
            calendar.add(Calendar.MINUTE, minute);
        }
        if (second != null) {
            calendar.add(Calendar.SECOND, second);
        }
        return calendar.getTime();
    }

    public static Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
}
