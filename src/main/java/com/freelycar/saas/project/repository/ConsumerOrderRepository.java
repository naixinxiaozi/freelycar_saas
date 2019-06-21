package com.freelycar.saas.project.repository;

import com.freelycar.saas.project.entity.ConsumerOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * @author tangwei - Toby
 * @date 2018/10/23
 * @email toby911115@gmail.com
 */
public interface ConsumerOrderRepository extends JpaRepository<ConsumerOrder, String> {
    List<ConsumerOrder> findAllByClientIdAndDelStatusOrderByCreateTimeDesc(String clientId, boolean delStatus);

    List<ConsumerOrder> findAllByClientIdAndDelStatusAndOrderTypeOrderByCreateTimeDesc(String clientId, boolean delStatus, Integer orderType);

    List<ConsumerOrder> findAllByClientIdAndDelStatusAndPayStateOrderByCreateTimeDesc(String clientId, boolean delStatus, int payState);

    Page<ConsumerOrder> findAll(Specification specification, Pageable pageable);

    ConsumerOrder findTopByClientIdAndOrderTypeAndDelStatusAndStateLessThan(String clientId, int orderType, boolean delStatus, int state);

    List<ConsumerOrder> findAllByStoreIdAndOrderTypeAndStateAndDelStatusAndLicensePlateContainingOrderByCreateTimeAsc(String storeId, int orderType, int state, boolean delStatus, String licensePlate);

    List<ConsumerOrder> findAllByStoreIdAndOrderTypeAndStateAndDelStatusAndLicensePlateContainingOrderByPickTimeAsc(String storeId, int orderType, int state, boolean delStatus, String licensePlate);

    @Query(value = "SELECT cast( sum( co.actualPrice ) AS DECIMAL ( 15, 2 ) ) AS result FROM consumerorder co WHERE co.delStatus = 0 AND co.payState = 2 AND co.storeId = :storeId AND co.isMember = :isMember  AND co.createTime >= :startTime AND co.createTime <= :endTime ", nativeQuery = true)
    Map sumIncomeForOneStoreByMember(String storeId, int isMember, String startTime, String endTime);

    @Query(value = "SELECT cast( sum( co.actualPrice ) AS DECIMAL ( 15, 2 ) ) AS result FROM consumerorder co WHERE co.delStatus = 0 AND co.payState = 2 AND co.isMember = :isMember  AND co.createTime >= :startTime AND co.createTime <= :endTime ", nativeQuery = true)
    Map sumIncomeForAllStoreByMember(int isMember, String startTime, String endTime);

    @Query(value = "SELECT cast( sum( co.actualPrice ) AS DECIMAL ( 15, 2 ) ) AS result FROM consumerorder co WHERE co.delStatus = 0 AND co.payState = 2 AND co.storeId = :storeId AND co.createTime >= :startTime AND co.createTime <= :endTime ", nativeQuery = true)
    Map sumAllIncomeForOneStore(String storeId, String startTime, String endTime);

    @Query(value = "SELECT cast( sum( co.actualPrice ) AS DECIMAL ( 15, 2 ) ) AS result FROM consumerorder co WHERE co.delStatus = 0 AND co.payState = 2 AND co.createTime >= :startTime AND co.createTime <= :endTime ", nativeQuery = true)
    Map sumAllIncomeForAllStore(String startTime, String endTime);

    @Query(value = "select cast( sum( co.firstActualPrice ) AS DECIMAL ( 15, 2 ) ) AS result  from consumerorder co where co.delStatus=0 and co.payState=2 and co.storeId = :storeId and co.firstPayMethod = :payMethod AND co.createTime >= :startTime AND co.createTime <= :endTime ", nativeQuery = true)
    Map sumFirstIncomeForOneStoreByPayMethod(String storeId, int payMethod, String startTime, String endTime);

    @Query(value = "select cast( sum( co.firstActualPrice ) AS DECIMAL ( 15, 2 ) ) AS result  from consumerorder co where co.delStatus=0 and co.payState=2 and co.firstPayMethod = :payMethod AND co.createTime >= :startTime AND co.createTime <= :endTime ", nativeQuery = true)
    Map sumFirstIncomeForAllStoreByPayMethod(int payMethod, String startTime, String endTime);

    @Query(value = "select cast( sum( co.secondActualPrice ) AS DECIMAL ( 15, 2 ) ) AS result  from consumerorder co where co.delStatus=0 and co.payState=2 and co.storeId = :storeId and co.secondPayMethod = :payMethod AND co.createTime >= :startTime AND co.createTime <= :endTime ", nativeQuery = true)
    Map sumSecondIncomeForOneStoreByPayMethod(String storeId, int payMethod, String startTime, String endTime);

    @Query(value = "select cast( sum( co.secondActualPrice ) AS DECIMAL ( 15, 2 ) ) AS result  from consumerorder co where co.delStatus=0 and co.payState=2 and co.secondPayMethod = :payMethod AND co.createTime >= :startTime AND co.createTime <= :endTime ", nativeQuery = true)
    Map sumSecondIncomeForAllStoreByPayMethod(int payMethod, String startTime, String endTime);

    int countAllByPickCarStaffIdAndDelStatusAndOrderType(String staffId, boolean delStatus, int orderType);
}
