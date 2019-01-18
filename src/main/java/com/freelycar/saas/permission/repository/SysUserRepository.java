package com.freelycar.saas.permission.repository;

import com.freelycar.saas.permission.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author tangwei [toby911115@gmail.com]
 * @date 2018/9/27
 */
public interface SysUserRepository extends JpaRepository<SysUser, Long> {
    SysUser findByUsername(String username);

    SysUser findByUsernameAndPassword(String username, String password);

}
