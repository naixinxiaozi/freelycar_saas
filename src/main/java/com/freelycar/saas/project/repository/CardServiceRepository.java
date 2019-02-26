package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.CardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2018/10/19
 * @email toby911115@gmail.com
 */
public interface CardServiceRepository extends JpaRepository<CardService, String> {
    @Query(value = "select * from cardService where id != :id and storeId = :storeId and delStatus = 0 and name = :name", nativeQuery = true)
    List<CardService> checkRepeatName(String id, String name, String storeId);

    @Query(value = "select * from cardService where storeId = :storeId and delStatus = 0 and name = :name", nativeQuery = true)
    List<CardService> checkRepeatName(String name, String storeId);

    Page<CardService> findAllByDelStatusAndStoreIdAndNameContaining(boolean delStatus, String storeId, String name,Pageable pageable);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update cardService set delStatus = 1 where id=:id", nativeQuery = true)
    int delById(String id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update cardService set bookOnline = 1 where id=:id", nativeQuery = true)
    int uppById(String id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update cardService set bookOnline = 0 where id=:id", nativeQuery = true)
    int lowById(String id);

    List<CardService> findByStoreIdAndDelStatusAndBookOnline(String storeId, boolean delStatus, boolean bookOnline);
}
