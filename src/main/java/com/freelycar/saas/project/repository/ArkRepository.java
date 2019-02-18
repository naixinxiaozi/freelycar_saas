package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.Ark;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author tangwei - Toby
 * @date 2019-02-18
 * @email toby911115@gmail.com
 */
public interface ArkRepository extends JpaRepository<Ark, String> {
    Ark findTopBySnAndDelStatus(String sn, boolean delStatus);
}
