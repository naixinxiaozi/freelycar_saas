package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.ConsumerOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2018/10/23
 * @email toby911115@gmail.com
 */
public interface ConsumerOrderRepository extends JpaRepository<ConsumerOrder, String> {
    List<ConsumerOrder> findAllByClientIdAndDelStatusOrderByCreateTimeDesc(String clientId, boolean delStatus);

    List<ConsumerOrder> findAllByClientIdAndDelStatusAndOrderTypeOrderByCreateTimeDesc(String clientId, boolean delStatus, Integer orderType);

    Page<ConsumerOrder> findAll(Specification specification, Pageable pageable);
}
