package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2018/10/17
 * @email toby911115@gmail.com
 */
public interface ClientRepository extends JpaRepository<Client,String> {

    List<Client> findByIdAndDelStatus(String id, boolean delStatus);


}
