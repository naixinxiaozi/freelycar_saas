package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.OrderSn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author tangwei - Toby
 * @date 2019-01-28
 * @email toby911115@gmail.com
 */
public interface OrderSnRepository extends JpaRepository<OrderSn, Long> {
    OrderSn findTopByStoreIdAndAndDateNumberOrderByCreateTimeDesc(String orderId, String dateNumber);

    OrderSn findTopByStoreIdOrderByCreateTimeDesc(String orderId);

    @Query(value = "select max(storeSn)+1 as storeSn from ordersn", nativeQuery = true)
    int generateStoreSn();
}
