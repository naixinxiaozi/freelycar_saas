package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2018/10/17
 * @email toby911115@gmail.com
 */
public interface StoreRepository extends JpaRepository<Store, String> {
    List<Store> findStoreByDelStatusAndNameContainingOrderByCreateTimeAsc(boolean delStatus, String name);

    List<Store> findAllByDelStatus(boolean delStatus);
}
