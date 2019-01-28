package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.OrderSn;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author tangwei - Toby
 * @date 2019-01-28
 * @email toby911115@gmail.com
 */
public interface OrderSnRepository extends JpaRepository<OrderSn, Long> {
    OrderSn findTopByStoreIdAndAndDateNumberOrderByCreateTimeDesc(String orderId, String dateNumber);

    OrderSn findTopByStoreIdOrderByCreateTimeDesc(String orderId);
}
