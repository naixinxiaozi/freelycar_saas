package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.Store;
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
public interface StoreRepository extends JpaRepository<Store, String> {
    List<Store> findStoreByDelStatusAndNameContainingOrderBySortAsc(boolean delStatus, String name);

    Page<Store> findStoreByDelStatusAndNameContainingOrderBySortAsc(boolean delStatus, String name, Pageable pageable);

    List<Store> findAllByDelStatusOrderBySortAsc(boolean delStatus);

    Store findTopByDelStatusAndSortIsNotNullOrderBySortDesc(boolean delStatus);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update store set del_status = 1 where id=:id", nativeQuery = true)
    int delById(String id);
}
