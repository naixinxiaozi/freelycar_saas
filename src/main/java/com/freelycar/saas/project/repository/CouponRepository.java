package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author tangwei - Toby
 * @date 2018/10/23
 * @email toby911115@gmail.com
 */
public interface CouponRepository extends JpaRepository<Coupon, String> {
}
