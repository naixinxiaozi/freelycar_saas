package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2018/10/18
 * @email toby911115@gmail.com
 */
public interface ProjectRepository extends JpaRepository<Project, String> {
    @Query(value = "select * from project where id != :id and store_id = :storeId and del_status = 0 and name = :name",nativeQuery = true)
    List<Project> checkRepeatName(String id, String name, String storeId);

    @Query(value = "select * from project where store_id = :storeId and del_status = 0 and name = :name",nativeQuery = true)
    List<Project> checkRepeatName(String name,String storeId);

    Page<Project> findAllByDelStatusAndStoreIdOrderByCreateTimeAsc(boolean delStatus, String storeId, Pageable pageable);
}
