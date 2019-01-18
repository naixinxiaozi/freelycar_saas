package com.freelycar.saas.jwt;

import com.freelycar.saas.basic.wrapper.ResultCode;
import com.freelycar.saas.jwt.bean.GrantedAuthorityImpl;
import com.freelycar.saas.permission.entity.SysUser;
import com.freelycar.saas.permission.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

/**
 * @author tangwei - Toby
 * @date 2018-12-17
 * @email toby911115@gmail.com
 */
class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private SysUserService sysUserService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取认证的用户名 & 密码
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(password)) {
            throw new BadCredentialsException(ResultCode.PARAM_NOT_COMPLETE.message());
        }

        SysUser sysUser = sysUserService.login(name, password);

        // 认证逻辑
        if (null != sysUser || name.equals("admin") && password.equals("freelyC@r2019")) {

            // 这里设置权限和角色
            // TODO 这里第二版需要改成从库里面去关联
            ArrayList<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new GrantedAuthorityImpl("ROLE_ADMIN"));
            authorities.add(new GrantedAuthorityImpl("AUTH_WRITE"));
            // 生成令牌
            Authentication auth = new UsernamePasswordAuthenticationToken(name, password, authorities);
            return auth;
        } else {
            throw new BadCredentialsException(ResultCode.USER_LOGIN_ERROR.message());
        }
    }

    // 是否可以提供输入类型的认证服务
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
