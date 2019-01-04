package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.CarType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2019-01-04
 * @email toby911115@gmail.com
 */
public interface CarTypeRepository extends JpaRepository<CarType, Integer> {
    List<CarType> findAllByCarBrandId(Integer carBrandId);
}
