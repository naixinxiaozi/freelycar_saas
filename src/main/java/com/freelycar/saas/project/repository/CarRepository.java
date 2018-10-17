package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author tangwei - Toby
 * @date 2018/10/17
 * @email toby911115@gmail.com
 */
public interface CarRepository extends JpaRepository<Car,String> {
}
