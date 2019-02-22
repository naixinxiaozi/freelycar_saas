package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.SpecialOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2019-02-22
 * @email toby911115@gmail.com
 */
public interface SpecialOfferRepository extends JpaRepository<SpecialOffer, String> {

    List<SpecialOffer> findByDelStatusOrderBySort(boolean delStatus);
}
