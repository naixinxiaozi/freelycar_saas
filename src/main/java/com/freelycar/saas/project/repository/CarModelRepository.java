package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2019-01-04
 * @email toby911115@gmail.com
 */
public interface CarModelRepository extends JpaRepository<CarModel, Integer> {
    List<CarModel> findAllByCarTypeId(Integer carTypeId);
}
