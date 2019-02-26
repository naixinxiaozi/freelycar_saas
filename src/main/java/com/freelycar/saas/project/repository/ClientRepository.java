package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2018/10/17
 * @email toby911115@gmail.com
 */
public interface ClientRepository extends JpaRepository<Client,String> {
    List<Client> findByIdAndDelStatus(String id, boolean delStatus);

    Page<Client> findAllByDelStatusAndStoreIdAndNameContainingAndPhoneContainingAndIsMember(boolean delStatus, String storeId, String name,String phone,boolean isMember, Pageable pageable);

    @Query(value = "SELECT client.name FROM client LEFT JOIN car on client.id=car.clientId LEFT JOIN card on client.id=card.clientId where car.defaultCar=1 GROUP BY card.clientId;", nativeQuery = true)
    String findName(String name);

    @Query(value = "SELECT phone FROM client LEFT JOIN car on client.id=car.clientId LEFT JOIN card on client.id=card.clientId where car.defaultCar=1 GROUP BY card.clientId;", nativeQuery = true)
    String findPhone(String phone);

    @Query(value = "SELECT licensePlate FROM client LEFT JOIN car on client.id=car.clientId LEFT JOIN card on client.id=card.clientId where car.defaultCar=1 GROUP BY card.clientId;", nativeQuery = true)
    String findLicensePlate(String licensePlate);

    List<Client> findByPhoneAndStoreIdAndDelStatusOrderByCreateTimeAsc(String phone, String storeId, boolean delStatus);

    List<Client> findByPhoneAndDelStatusOrderByCreateTimeAsc(String phone, boolean delStatus);

    Client findTopByPhoneAndStoreIdAndDelStatusOrderByCreateTimeAsc(String phone, String storeId, boolean delStatus);

    Client findTopByPhoneAndDelStatusOrderByCreateTimeAsc(String phone, boolean delStatus);

    int countByDelStatusAndStoreIdAndIsMember(boolean delStatus, String storeId, boolean isMember);

    int countByDelStatusAndStoreIdAndIsMemberAndMemberDateBetween(boolean delStatus, String storeId, boolean isMember, Timestamp memberDateStart, Timestamp memberDateEnd);

    @Transactional
    @Modifying
    @Query(value = "update client set delStatus=1 where id=:id", nativeQuery = true)
    int delById(String id);
}
