package com.freelycar.saas.permission.service;

import com.freelycar.saas.basic.wrapper.ResultCode;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.permission.entity.SysUser;
import com.freelycar.saas.permission.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author tangwei [toby911115@gmail.com]
 * @date 2018/9/29
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RegisterServcie {
    @Autowired
    SysUserRepository userRepository;

    /**
     * 注册系统用户
     * @param sysUser
     * @return
     */
    public ResultJsonObject register(SysUser sysUser) {
        if (null != sysUser) {
            if (isExist(sysUser)) {
                return ResultJsonObject.getErrorResult(null, ResultCode.USER_HAS_EXISTED.message());
            }
            SysUser resultUser = userRepository.save(encryptPassword(sysUser));
            return ResultJsonObject.getDefaultResult(resultUser, ResultCode.SUCCESS.message());
        }
        return ResultJsonObject.getErrorResult(null);
    }

    /**
     * 用户名验重
     * @param sysUser
     * @return  true/false
     */
    private boolean isExist(SysUser sysUser) {
        String userName = sysUser.getUsername();
        SysUser user = userRepository.findByUsername(userName);
        if (null == user) {
            return false;
        }
        return true;
    }

    /**
     * 加密密码(BCrypt)
     */
    private SysUser encryptPassword(SysUser sysUser) {
        String password = sysUser.getPassword();
        password = new BCryptPasswordEncoder().encode(password);
        sysUser.setPassword(password);
        return sysUser;
    }
}
