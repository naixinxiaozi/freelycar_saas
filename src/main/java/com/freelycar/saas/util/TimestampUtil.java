package com.freelycar.saas.util;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * @author tangwei - Toby
 * @date 2019-01-16
 * @email toby911115@gmail.com
 */

public class TimestampUtil {

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
}
