package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.ConsumerProjectInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2018/10/23
 * @email toby911115@gmail.com
 */
public interface ConsumerProjectInfoRepository extends JpaRepository<ConsumerProjectInfo,String> {

    List<ConsumerProjectInfo> findAllByDelStatusAndConsumerOrderIdOrderByCreateTimeAsc(boolean delStatus, String consumerOrderId);

    List<ConsumerProjectInfo> findAllByDelStatusAndAndCardIdOrderByCreateTimeDesc(boolean delStatus, String cardId);

    List<ConsumerProjectInfo> findAllByDelStatusAndCouponIdOrderByCreateTimeDesc(boolean delStatus, String couponId);
}
