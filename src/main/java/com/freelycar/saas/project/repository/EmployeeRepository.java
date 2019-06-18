package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author tangwei - Toby
 * @date 2019-06-17
 * @email toby911115@gmail.com
 */
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    Employee findTopByAccountAndPasswordAndDelStatus(String account, String password, boolean delStatus);

    Employee findTopByPhoneAndDelStatus(String phone, boolean delStatus);
}
