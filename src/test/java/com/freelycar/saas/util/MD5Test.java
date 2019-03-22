package com.freelycar.saas.util;


import com.freelycar.saas.wxutils.MD5;

/**
 * @author tangwei - Toby
 * @date 2019-03-22
 * @email toby911115@gmail.com
 */
public class MD5Test {
    public static void main(String[] args) {
        System.out.println("sysadmin:");
        System.out.println(MD5.compute("sysadmin"));
        System.out.println("admin:");
        System.out.println(MD5.compute("admin"));

    }
}
