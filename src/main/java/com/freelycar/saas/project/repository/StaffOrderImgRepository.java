package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.StaffOrderImg;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author tangwei - Toby
 * @date 2019-06-04
 * @email toby911115@gmail.com
 */
public interface StaffOrderImgRepository extends JpaRepository<StaffOrderImg, Long> {
    StaffOrderImg findTopByOrderIdAndDelStatusOrderByCreateTimeDesc(String orderId, boolean delStatus);
}
