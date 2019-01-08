package com.freelycar.saas.util;

import java.math.BigDecimal;

/**
 * @author tangwei - Toby
 * @date 2019-01-08
 * @email toby911115@gmail.com
 * <p>
 * 与小数位精度(四舍五入等)相关的一些常用工具方法.
 * <p>
 * float/double的精度取值方式分为以下几种:
 * java.math.BigDecimal.ROUND_UP
 * java.math.BigDecimal.ROUND_DOWN
 * java.math.BigDecimal.ROUND_CEILING
 * java.math.BigDecimal.ROUND_FLOOR
 * java.math.BigDecimal.ROUND_HALF_UP
 * java.math.BigDecimal.ROUND_HALF_DOWN
 * java.math.BigDecimal.ROUND_HALF_EVEN
 */
public class RoundTool {
    /**
     * 对double数据进行取精度.
     * <p>
     * For example:
     * double value = 100.345678;
     * double ret = round(value,4,BigDecimal.ROUND_HALF_UP);
     * ret为100.3457
     *
     * @param value        double数据.
     * @param scale        精度位数(保留的小数位数).
     * @param roundingMode 精度取值方式.
     * @return 精度计算后的数据.
     */
    public static double round(double value, int scale, int roundingMode) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(scale, roundingMode);
        double d = bd.doubleValue();
        bd = null;
        return d;
    }

    public static float round(float value, int scale, int roundingMode) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(scale, roundingMode);
        float d = bd.floatValue();
        bd = null;
        return d;
    }
}
