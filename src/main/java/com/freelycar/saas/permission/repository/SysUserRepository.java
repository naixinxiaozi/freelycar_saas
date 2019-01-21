package com.freelycar.saas.permission.repository;

import com.freelycar.saas.permission.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author tangwei [toby911115@gmail.com]
 * @date 2018/9/27
 */
public interface SysUserRepository extends JpaRepository<SysUser, Long> {
    SysUser findByUsername(String username);

    List<SysUser> findByUsernameAndDelStatus(String userName, boolean delStatus);

    List<SysUser> findByUsernameAndDelStatusAndIdNot(String userName, boolean delStatus, Long id);

    SysUser findByDelStatusAndUsernameAndPassword(boolean delStatus, String username, String password);

}
