package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.CardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2018/10/19
 * @email toby911115@gmail.com
 */
public interface CardServiceRepository extends JpaRepository<CardService, String> {
    @Query(value = "select * from card_service where id != :id and store_id = :storeId and del_status = 0 and name = :name",nativeQuery = true)
    List<CardService> checkRepeatName(String id, String name, String storeId);

    @Query(value = "select * from card_service where store_id = :storeId and del_status = 0 and name = :name",nativeQuery = true)
    List<CardService> checkRepeatName(String name,String storeId);

    Page<CardService> findAllByDelStatusAndStoreIdOrderByCreateTimeAsc(boolean delStatus, String storeId, Pageable pageable);
}
