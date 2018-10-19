package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author tangwei - Toby
 * @date 2018/10/19
 * @email toby911115@gmail.com
 */
public interface CardRepository extends JpaRepository<Card, String> {
}
