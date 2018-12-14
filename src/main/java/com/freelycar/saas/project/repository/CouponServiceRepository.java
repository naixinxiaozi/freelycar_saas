package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.CouponService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2018/10/23
 * @email toby911115@gmail.com
 */
public interface CouponServiceRepository extends JpaRepository<CouponService, String> {
    @Query(value = "select * from coupon_service where id != :id and store_id = :storeId and del_status = 0 and name = :name",nativeQuery = true)
    List<CouponService> checkRepeatName(String id, String name, String storeId);

    @Query(value = "select * from coupon_service where store_id = :storeId and del_status = 0 and name = :name",nativeQuery = true)
    List<CouponService> checkRepeatName(String name,String storeId);

    Page<CouponService> findAllByDelStatusAndStoreIdOrderByCreateTimeAsc(boolean delStatus, String storeId, Pageable pageable);
}
