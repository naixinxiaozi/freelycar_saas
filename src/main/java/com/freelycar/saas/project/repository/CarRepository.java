package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2018/10/17
 * @email toby911115@gmail.com
 */
public interface CarRepository extends JpaRepository<Car,String> {
    List<Car> findByClientIdAndDelStatus(String clientId, boolean delStatus);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update car set del_status = 1 where id=:id", nativeQuery = true)
    int delById(String id);

    @Query(value = "select * from car where id != :id and store_id = :storeId and del_status = 0 and license_plate = :license_plate",nativeQuery = true)
    List<Car> checkRepeatName(String id, String license_plate, String storeId);

    @Query(value = "select * from car where store_id = :storeId and del_status = 0 and license_plate = :license_plate",nativeQuery = true)
    List<Car> checkRepeatName(String license_plate,String storeId);

}
