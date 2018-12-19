package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2018/10/17
 * @email toby911115@gmail.com
 */
public interface StaffRepository extends JpaRepository<Staff,String> {
    @Query(value = "select * from staff where id != :id and store_id = :storeId and del_status = 0 and name = :name",nativeQuery = true)
    List<Staff> checkRepeatName(String id, String name, String storeId);

    @Query(value = "select * from staff where store_id = :storeId and del_status = 0 and name = :name",nativeQuery = true)
    List<Staff> checkRepeatName(String name,String storeId);

    Page<Staff> findAllByDelStatusAndStoreIdOrderByCreateTimeAsc(boolean delStatus, String storeId, Pageable pageable);
}
