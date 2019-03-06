package com.freelycar.saas.util;

import org.apache.http.util.TextUtils;

/**
 * @author tangwei - Toby
 * @date 2019-03-06
 * @email toby911115@gmail.com
 */
public class CarNumberTool {

    public static boolean isCarNumberNO(String carnumber) {
        String carnumRegex = "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}(([0-9]{5}[DF])|([DF]([A-HJ-NP-Z0-9])[0-9]{4})))|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳]{1})";
        if (TextUtils.isEmpty(carnumber)) {
            return false;
        } else {
            return carnumber.matches(carnumRegex);
        }

    }
}
