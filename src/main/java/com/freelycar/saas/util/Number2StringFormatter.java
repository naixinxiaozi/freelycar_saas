package com.freelycar.saas.util;

import com.freelycar.saas.exception.NumberOutOfRangeException;

/**
 * @author tangwei - Toby
 * @date 2019-05-09
 * @email toby911115@gmail.com
 */
public class Number2StringFormatter {

    /**
     * 格式化数字为四位数字符串格式
     *
     * @param number
     * @return
     */
    public static String format4Number2String(int number) throws NumberOutOfRangeException {
        if (number < 1) {
            return "0001";
        }
        if (number < 10) {
            return "000" + number;
        }
        if (number < 100) {
            return "00" + number;
        }
        if (number < 1000) {
            return "0" + number;
        }
        if (number > 9999) {
            throw new NumberOutOfRangeException("数字 " + number + " 超过4位无法转换");
        }
        return String.valueOf(number);
    }

    /**
     * 格式化数字为三位数字符串格式
     *
     * @param number
     * @return
     */
    public static String format3Number2String(int number) throws NumberOutOfRangeException {
        if (number < 1) {
            return "001";
        }
        if (number < 10) {
            return "00" + number;
        }
        if (number < 100) {
            return "0" + number;
        }
        if (number > 999) {
            throw new NumberOutOfRangeException("数字 " + number + " 超过3位无法转换");
        }
        return String.valueOf(number);
    }
}
