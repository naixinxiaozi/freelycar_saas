package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.Constants;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.exception.ArgumentMissingException;
import com.freelycar.saas.exception.ObjectNotFoundException;
import com.freelycar.saas.project.entity.ConsumerOrder;
import com.freelycar.saas.project.entity.ConsumerProjectInfo;
import com.freelycar.saas.project.entity.Coupon;
import com.freelycar.saas.project.repository.ConsumerOrderRepository;
import com.freelycar.saas.project.repository.ConsumerProjectInfoRepository;
import com.freelycar.saas.project.repository.CouponRepository;
import com.freelycar.saas.project.repository.CouponServiceRepository;
import com.freelycar.saas.util.TimestampUtil;
import com.freelycar.saas.util.UpdateTool;
import com.freelycar.saas.wechat.model.CouponInfo;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author tangwei - Toby
 * @date 2019-01-10
 * @email toby911115@gmail.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CouponService {

    private Logger logger = LoggerFactory.getLogger(CouponService.class);

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponServiceRepository couponServiceRepository;

    @Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;

    @Autowired
    private ConsumerProjectInfoRepository consumerProjectInfoRepository;

    @Autowired
    private ConsumerOrderRepository consumerOrderRepository;


    /**
     * 新增或修改
     *
     * @param coupon
     * @return
     */
    public Coupon saveOrUpdate(Coupon coupon) {
        if (null == coupon) {
            return null;
        }

        String id = coupon.getId();
        if (StringUtils.isEmpty(id)) {
            coupon.setDelStatus(Constants.DelStatus.NORMAL.isValue());
            coupon.setCreateTime(new Timestamp(System.currentTimeMillis()));
            //计算deadline（截止日期）
            com.freelycar.saas.project.entity.CouponService couponServiceObject = couponServiceRepository.getOne(coupon.getCouponServiceId());
            if (null == couponServiceObject) {
                return null;
            }
            coupon.setDeadline(TimestampUtil.getExpirationDateForMonth(couponServiceObject.getValidTime()));
        } else {
            Optional<Coupon> couponOptional = couponRepository.findById(id);
            if (!couponOptional.isPresent()) {
                return null;
            }
            Coupon source = couponOptional.get();
            UpdateTool.copyNullProperties(source, coupon);
        }
        return couponRepository.save(coupon);
    }

    /**
     * 查询与某个订单关联的所有抵用券
     *
     * @param orderId
     * @return
     */
    public List<Coupon> findCouponByOrderId(String orderId) {
        return couponRepository.findByOrderIdAndStatus(orderId, Constants.CouponStatus.KEEP.getValue());
    }

    /**
     * 查询所有关联的抵用券，并将所有orderId和status初始化
     *
     * @param orderId
     */
    public void initCouponByOrderId(String orderId) {
        List<Coupon> oldCoupons = this.findCouponByOrderId(orderId);
        for (Coupon coupon : oldCoupons) {
            coupon.setOrderId(null);
            coupon.setStatus(Constants.CouponStatus.NOT_USE.getValue());
            this.saveOrUpdate(coupon);
        }
    }

    /**
     * 查询某客户的所有有效优惠券
     *
     * @param clientId
     * @param storeId
     * @return
     */
    public ResultJsonObject getMyCoupons(String clientId, String storeId) {
        if (StringUtils.isEmpty(storeId)) {
            return ResultJsonObject.getErrorResult(null, "查询失败：参数storeId为空值");
        }
        if (StringUtils.isEmpty(clientId)) {
            return ResultJsonObject.getErrorResult(null, "查询失败：参数clientId为空值");
        }
//        List<Coupon> res = couponRepository.findByClientIdAndDelStatusAndStatusAndStoreId(clientId, Constants.DelStatus.NORMAL.isValue(), Constants.CouponStatus.NOT_USE.getValue(), storeId);
//        return ResultJsonObject.getDefaultResult(res);

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT c.id,c.name,c.price,c.deadline,c.status,c.content,c.clientId,c.couponServiceId,c.storeId, p.price AS originalPrice FROM coupon c LEFT JOIN project p ON p.id = c.projectId WHERE c.delStatus = 0 ")
                .append("  AND c.clientId = '").append(clientId).append("' ")
                .append("  AND c.storeId = '").append(storeId).append("' ")
                .append(" ORDER BY c.createTime DESC ");

        EntityManager em = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
        Query nativeQuery = em.createNativeQuery(sql.toString());
        nativeQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(CouponInfo.class));
        @SuppressWarnings({"unused", "unchecked"})
        List<CouponInfo> res = nativeQuery.getResultList();

        //关闭em
        em.close();

        return ResultJsonObject.getDefaultResult(res);
    }

    /**
     * 查询某订单下，所有项目对应的抵用券
     *
     * @param orderId
     * @return
     * @throws ObjectNotFoundException
     * @throws ArgumentMissingException
     */
    public List<Coupon> findAllUsefulCouponForOneOrder(String orderId) throws ObjectNotFoundException, ArgumentMissingException {
        List<Coupon> resultList = new ArrayList<>();
        if (StringUtils.isEmpty(orderId)) {
            logger.error("参数orderId为空值，获取结算抵用券失败，返回空集合。");
            return resultList;
        }
        //查询order信息
        ConsumerOrder consumerOrder = consumerOrderRepository.findById(orderId).orElseThrow(ObjectNotFoundException::new);

        String clientId = consumerOrder.getClientId();
        String storeId = consumerOrder.getStoreId();
        if (StringUtils.isEmpty(clientId)) {
            throw new ArgumentMissingException("查询到的order对象中，clientId为空，无法加载可用抵用券");
        }
        if (StringUtils.isEmpty(storeId)) {
            throw new ArgumentMissingException("查询到的order对象中，storeId为空，无法加载可用抵用券");
        }

        //查询有没有挂单的在这个订单下的抵用券，要排除掉这几个项目
        List<Coupon> keepingCoupons = couponRepository.findByOrderIdAndStatus(orderId, Constants.CouponStatus.KEEP.getValue());


        //查询单据中有几个项目
        List<ConsumerProjectInfo> consumerProjectInfos = consumerProjectInfoRepository.findAllByDelStatusAndConsumerOrderIdOrderByCreateTimeAsc(Constants.DelStatus.NORMAL.isValue(), orderId);

        for (ConsumerProjectInfo consumerProjectInfo : consumerProjectInfos) {
            String projectId = consumerProjectInfo.getProjectId();
            if (StringUtils.hasText(projectId)) {
                boolean hadCoupon = false;

                //如果这个项目已经有挂单的抵用券，直接返回这个挂单的抵用券
                for (Coupon keepingCoupon : keepingCoupons) {
                    if (projectId.equals(keepingCoupon.getProjectId())) {
                        resultList.add(keepingCoupon);
                        hadCoupon = true;
                        break;
                    }
                }

                //没有挂单的券的话，查一下有没有可用的抵用券
                if (!hadCoupon) {
                    Coupon usefulCoupon = couponRepository.findTopByClientIdAndDelStatusAndStatusAndStoreIdAndProjectIdOrderByDeadlineAsc(clientId, Constants.DelStatus.NORMAL.isValue(), Constants.CouponStatus.NOT_USE.getValue(), storeId, projectId);
                    if (null != usefulCoupon) {
                        resultList.add(usefulCoupon);
                    }
                }
            }
        }

        return resultList;
    }

    //查询某项目可以使用的抵用券

}
