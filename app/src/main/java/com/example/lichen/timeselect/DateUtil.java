package com.example.lichen.timeselect;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * 日期工具类
 *
 * @author huangziwei
 * @date 2016.1.13
 */
public class DateUtil {

    public static int getMonthDays(int year, int month) {
        if (month > 12) {
            month = 1;
            year += 1;
        } else if (month < 1) {
            month = 12;
            year -= 1;
        }
        int[] arr = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int days = 0;

        if (isLeapYear(year)) {
            arr[1] = 29; // 闰年2月29天
        }

        try {
            days = arr[month - 1];
        } catch (Exception e) {
            e.getStackTrace();
        }

        return days;
    }

    /**
     * 是否为闰年
     *
     * @param year
     * @return
     */
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }


    /**
     * 根据年份和月份获取日期数组，1、2、3...
     *
     * @param year
     * @param month
     * @return
     */
    public static List<String> getMonthDaysArray(int year, int month) {
        List<String> dayList = new ArrayList<String>();
        int days = DateUtil.getMonthDays(year, month);
        for (int i = 1; i <= days; i++) {
            dayList.add(i + "");
        }
        return dayList;
    }


    /**
     * 获取当前系统时间的年份
     *
     * @return
     */
    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * 获取当前系统时间的月份
     *
     * @return
     */
    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前系统时间的月份的第几天
     *
     * @return
     */
    public static int getCurrentMonthDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前系统时间的小时数
     *
     * @return
     */
    public static int getHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取当前系统时间的分钟数
     *
     * @return
     */
    public static int getMinute() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    /**
     * 获取当前系统时间的秒数
     *
     * @return
     */
    public static int getSecond() {
        return Calendar.getInstance().get(Calendar.SECOND);
    }

    /**
     * 根据系统默认时区，获取当前时间与time的天数差
     *
     * @param time
     * @return　等于０表示今天，大于０表示今天之前
     */
    public static long getDaySpan(long time) {
        // 系统默认时区，ms
        int tiemzone = TimeZone.getDefault().getRawOffset();
        // １天＝24*60*60*1000ms
        return (System.currentTimeMillis() + tiemzone) / 86400000
                - (time + tiemzone) / 86400000;
    }

    public static boolean isToday(long time) {
        return getDaySpan(time) == 0;
    }

    public static boolean isYestoday(long time) {
        return getDaySpan(time) == 1;
    }

    /**
     * @return 返回当前时间，yyyy-MM-dd HH-mm-ss
     */
    public static String getDate() {
        return getDate("yyyy-MM-dd HH-mm-ss");
    }

    public static String getDate(String format) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(format);
        String date = sDateFormat.format(new java.util.Date());
        return date;
    }
}