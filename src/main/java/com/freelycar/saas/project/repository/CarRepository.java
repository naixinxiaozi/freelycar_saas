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
public interface CarRepository extends JpaRepository<Car, String> {
    List<Car> findByClientIdAndDelStatus(String clientId, boolean delStatus);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update car set delStatus = 1 where id=:id", nativeQuery = true)
    int delById(String id);

    @Query(value = "select * from car where id != :id and storeId = :storeId and delStatus = 0 and licensePlate = :licensePlate", nativeQuery = true)
    List<Car> checkRepeatName(String id, String licensePlate, String storeId);

    @Query(value = "select * from car where storeId = :storeId and delStatus = 0 and licensePlate = :licensePlate", nativeQuery = true)
    List<Car> checkRepeatName(String licensePlate, String storeId);

    @Query(value = "SELECT car.* FROM car LEFT JOIN client c ON c.id = car.clientId WHERE phone = :phone AND car.delStatus = 0 GROUP BY car.licensePlate ORDER BY car.defaultCar DESC, car.createTime ASC", nativeQuery = true)
    List<Car> listCarsByStoreIdWithoutSamePlate(String phone);

    @Query(value = "SELECT car.* FROM car LEFT JOIN client c ON c.id = car.clientId WHERE c.storeId=:storeId AND c.phone = :phone AND car.delStatus = 0 ORDER BY car.defaultCar DESC, car.createTime ASC", nativeQuery = true)
    List<Car> listCarsByStoreIdAndPhone(String storeId, String phone);

    Car findTopByLicensePlateAndStoreIdAndAndDelStatusOrderByCreateTimeDesc(String licensePlate, String storeId, boolean delStatus);
}
