package com.freelycar.saas.permission.service;

import com.freelycar.saas.permission.entity.SysUser;
import com.freelycar.saas.permission.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author tangwei - Toby
 * @date 2019-01-18
 * @email toby911115@gmail.com
 */
@Service
public class SysUserService {

    @Autowired
    private SysUserRepository sysUserRepository;

    public SysUser login(String userName, String password) {
        return sysUserRepository.findByUsernameAndPassword(userName, password);
    }
}
