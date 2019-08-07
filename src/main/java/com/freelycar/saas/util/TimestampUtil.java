package com.freelycar.saas.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author tangwei - Toby
 * @date 2019-01-16
 * @email toby911115@gmail.com
 */

public class TimestampUtil {

    private static final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
    static final SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取有效期的截止日期（年）
     *
     * @param validYear
     * @return
     */
    public static Timestamp getExpirationDateForYear(int validYear) {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int year = currentYear + validYear;
        calendar.set(Calendar.YEAR, year);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 获取有效期的截止日期（月）
     *
     * @param validMonth
     * @return
     */
    public static Timestamp getExpirationDateForMonth(int validMonth) {
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        int month = currentMonth + validMonth;
        calendar.set(Calendar.MONTH, month);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 获得当前时间
     *
     * @return string
     */
    public static String getCurrentTime() {
        Date date = new Date();
        DateFormat mediumTime = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        return mediumTime.format(date);
    }

    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getCurrentDate() {
        Date date = new Date();
        return sdfDate.format(date);
    }

    /**
     * 获取某年某月的第一天
     *
     * @param year
     * @param month
     * @return
     */
    public static String getFisrtDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //获取某月最小天数
        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        //格式化日期
        return sdfDate.format(cal.getTime());
    }

    /**
     * 获取某月的最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public static String getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        return sdfDate.format(cal.getTime());
    }
}
