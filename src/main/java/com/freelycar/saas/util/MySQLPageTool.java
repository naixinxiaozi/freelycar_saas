package com.freelycar.saas.util;

/**
 * @author tangwei - Toby
 * @date 2019-01-17
 * @email toby911115@gmail.com
 */
public class MySQLPageTool {

    public static int getStartPosition(int page, int pageSize) {
        return (page - 1) * pageSize;
    }
}
