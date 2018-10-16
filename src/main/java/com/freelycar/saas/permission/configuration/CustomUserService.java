package com.freelycar.saas.permission.configuration;

import com.freelycar.saas.permission.entity.SysUser;
import com.freelycar.saas.permission.repository.SysUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author tangwei [toby911115@gmail.com]
 * @date 2018/9/27
 */
public class CustomUserService implements UserDetailsService {

    private static Logger logger = LoggerFactory.getLogger(CustomUserService.class);

    @Autowired
    SysUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        SysUser user = userRepository.findByUsername(s);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        logger.info("用户:" + s + "登录...");
        logger.debug("username:" + user.getUsername() + ";password:" + user.getPassword());
        return user;
    }
}
