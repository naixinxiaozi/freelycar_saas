package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.AutoParts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2018/12/5
 * @email toby911115@gmail.com
 */
public interface AutoPartsRepository extends JpaRepository<AutoParts, String> {
    List<AutoParts> findAllByDelStatusAndConsumerOrderIdOrderByCreateTimeAsc(boolean delStatus, String consumerOrderId);
}
