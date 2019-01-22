package com.freelycar.saas.permission.repository;

import com.freelycar.saas.permission.entity.SysUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Modifying
    @Query(value = "update sys_user set del_status=1 where id=:id", nativeQuery = true)
    int delById(long id);

    Page<SysUser> findByDelStatusAndStoreId(boolean delStatus, String storeId, Pageable pageable);

    Page<SysUser> findByDelStatus(boolean delStatus, Pageable pageable);
}
