package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.ClientOrderImg;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author tangwei - Toby
 * @date 2019-06-04
 * @email toby911115@gmail.com
 */
public interface ClientOrderImgRepository extends JpaRepository<ClientOrderImg, Long> {

    ClientOrderImg findTopByOrderIdAndDelStatusOrderByCreateTimeDesc(String orderId, boolean delStatus);
}
