package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.Constants;
import com.freelycar.saas.project.entity.Coupon;
import com.freelycar.saas.project.repository.CouponRepository;
import com.freelycar.saas.project.repository.CouponServiceRepository;
import com.freelycar.saas.util.TimestampUtil;
import com.freelycar.saas.util.UpdateTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * @author tangwei - Toby
 * @date 2019-01-10
 * @email toby911115@gmail.com
 */
@Service
@Transactional
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponServiceRepository couponServiceRepository;

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
}
