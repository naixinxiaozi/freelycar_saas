package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2018/10/19
 * @email toby911115@gmail.com
 */
public interface CardRepository extends JpaRepository<Card, String> {
    List<Card> findByClientIdAndDelStatus(String client, boolean delStatus);

    List<Card> findByClientIdAndDelStatusAndStoreId(String client, boolean delStatus, String storeId);

    List<Card> findByCardNumberAndDelStatusAndStoreId(String cardNumber, boolean delStatus, String storeId);

    @Query(value = "select sum(balance) from card where clientId=:clientId and delStatus=0", nativeQuery = true)
    Float sumBalanceByClientId(String clientId);

}
