package com.freelycar.saas.jwt.bean;

/**
 * 存储用户名密码
 *
 * @author tangwei - Toby
 * @date 2018-12-17
 * @email toby911115@gmail.com
 */
public class AccountCredentials {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
