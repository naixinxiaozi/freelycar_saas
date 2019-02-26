package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2018/10/18
 * @email toby911115@gmail.com
 */
public interface ProjectRepository extends JpaRepository<Project, String> {
    @Query(value = "select * from project where id != :id and storeId = :storeId and delStatus = 0 and name = :name", nativeQuery = true)
    List<Project> checkRepeatName(String id, String name, String storeId);

    @Query(value = "select * from project where storeId = :storeId and delStatus = 0 and name = :name", nativeQuery = true)
    List<Project> checkRepeatName(String name, String storeId);

    Page<Project> findAllByDelStatusAndStoreIdAndNameContainingAndProjectTypeId(boolean delStatus, String storeId, String name, String projectTypeId, Pageable pageable);

    Page<Project> findAllByDelStatusAndStoreIdAndNameContaining(boolean delStatus, String storeId, String name, Pageable pageable);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update project set delStatus = 1 where id=:id", nativeQuery = true)
    int delById(String id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update project set saleStatus = 1 where id=:id", nativeQuery = true)
    int uppArkById(String id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update project set saleStatus = 0 where id=:id", nativeQuery = true)
    int lowArkById(String id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update project set bookOnline = 1 where id=:id", nativeQuery = true)
    int uppById(String id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update project set bookOnline = 0 where id=:id", nativeQuery = true)
    int lowById(String id);

    List<Project> findAllByStoreIdAndDelStatusAndSaleStatusOrderByCreateTime(String storeId, boolean delStatus, boolean saleStatus);

    List<Project> findByStoreIdAndDelStatusAndBookOnline(String storeId, boolean delStatus, boolean bookOnline);
}
