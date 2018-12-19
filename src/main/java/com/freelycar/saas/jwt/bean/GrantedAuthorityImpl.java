package com.freelycar.saas.jwt.bean;

import org.springframework.security.core.GrantedAuthority;

/**
 * 负责存储权限和角色
 *
 * @author tangwei - Toby
 * @date 2018-12-17
 * @email toby911115@gmail.com
 */
public class GrantedAuthorityImpl implements GrantedAuthority {
    private String authority;

    public GrantedAuthorityImpl(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}