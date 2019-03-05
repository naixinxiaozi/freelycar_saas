package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2018/10/17
 * @email toby911115@gmail.com
 */
public interface StaffRepository extends JpaRepository<Staff,String> {
    @Query(value = "select * from staff where id != :id and storeId = :storeId and delStatus = 0 and name = :name", nativeQuery = true)
    List<Staff> checkRepeatName(String id, String name, String storeId);

    @Query(value = "select * from staff where storeId = :storeId and delStatus = 0 and name = :name", nativeQuery = true)
    List<Staff> checkRepeatName(String name,String storeId);


    Page<Staff> findAllByDelStatusAndStoreIdAndIdContainingAndNameContaining(boolean delStatus, String storeId,String id,String name, Pageable pageable);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update staff set delStatus = 1 where id=:id", nativeQuery = true)
    int delById(String id);

    @Query(value = "select * from staff where account=:account and id!=:id and isArk = 1", nativeQuery = true)
    List<Staff> checkRepeatAccount(String account,String id);

    @Query(value = "select * from staff where account=:account",nativeQuery = true)
    List<Staff> checkRepeatAccount(String account);

    Staff findTopByAccountAndPasswordAndDelStatus(String account, String password, boolean delStatus);


    List<Staff> findAllByDelStatusAndIsArkAndStoreId(boolean delStatus, boolean isArk, String storeId);


}
