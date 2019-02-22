package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.StoreImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2019-02-22
 * @email toby911115@gmail.com
 */
public interface StoreImgRepository extends JpaRepository<StoreImg, String> {
    List<StoreImg> findByStoreIdAndDelStatusOrderByCreateTimeAsc(String storeId, boolean delStatus);

}
