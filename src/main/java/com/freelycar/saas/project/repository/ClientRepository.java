package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2018/10/17
 * @email toby911115@gmail.com
 */
public interface ClientRepository extends JpaRepository<Client,String> {
    List<Client> findByIdAndDelStatus(String id, boolean delStatus);

    Page<Client> findAllByDelStatusAndStoreIdAndNameContainingAndPhoneContainingAndIsMember(boolean delStatus, String storeId, String name,String phone,boolean isMember, Pageable pageable);

//    @Query(value = "SELECT client.name,phone,license_plate,car_brand,is_member,consume_times,last_visit,sum(balance) FROM client LEFT JOIN car on client.id=car.client_id LEFT JOIN card on client.id=card.client_id where car.default_car=1 GROUP BY card.client_id;",nativeQuery = true)
//    Page<Client> asd(String name, String phone, String licensePlate, String carBrand, boolean isMember, Integer consumeTimes, DateTimeFormat lastVisit, Float balance);

    @Query(value = "SELECT client.name FROM client LEFT JOIN car on client.id=car.client_id LEFT JOIN card on client.id=card.client_id where car.default_car=1 GROUP BY card.client_id;",nativeQuery = true)
    String findName(String name);

    @Query(value = "SELECT phone FROM client LEFT JOIN car on client.id=car.client_id LEFT JOIN card on client.id=card.client_id where car.default_car=1 GROUP BY card.client_id;",nativeQuery = true)
    String findPhone(String phone);

    @Query(value = "SELECT license_plate FROM client LEFT JOIN car on client.id=car.client_id LEFT JOIN card on client.id=card.client_id where car.default_car=1 GROUP BY card.client_id;",nativeQuery = true)
    String findLicensePlate(String licensePlate);

    List<Client> findByPhoneAndStoreIdAndDelStatusOrderByCreateTimeAsc(String phone, String storeId, boolean delStatus);


}
