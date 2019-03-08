package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.*;
import com.freelycar.saas.exception.ArgumentMissingException;
import com.freelycar.saas.exception.ObjectNotFoundException;
import com.freelycar.saas.project.entity.Client;
import com.freelycar.saas.project.entity.ConsumerOrder;
import com.freelycar.saas.project.entity.Coupon;
import com.freelycar.saas.project.entity.CouponService;
import com.freelycar.saas.project.repository.CouponRepository;
import com.freelycar.saas.project.repository.CouponServiceRepository;
import com.freelycar.saas.util.RoundTool;
import com.freelycar.saas.util.UpdateTool;
import com.freelycar.saas.wechat.model.CouponInfo;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static com.freelycar.saas.basic.wrapper.ResultCode.RESULT_DATA_NONE;

@Service
@Transactional(rollbackFor = Exception.class)
public class CouponServiceService {
    private Logger logger = LoggerFactory.getLogger(CouponServiceService.class);

    @Autowired
    private ConsumerOrderService consumerOrderService;

    @Autowired
    private CouponServiceRepository couponServiceRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;

    /**
     * 新增/修改抵用券对象
     *
     * @param couponService
     * @return
     */
    public ResultJsonObject modify(CouponService couponService) {
        try {
            //验重
            if (this.checkRepeatName(couponService)) {
                return ResultJsonObject.getErrorResult(null, "已包含名称为：“" + couponService.getName() + "”的数据，不能重复添加。");
            }

            //是否有ID，判断时新增还是修改
            String id = couponService.getId();
            if (StringUtils.isEmpty(id)) {
                couponService.setDelStatus(Constants.DelStatus.NORMAL.isValue());
                couponService.setCreateTime(new Timestamp(System.currentTimeMillis()));
                // couponService.setBookOnline(false);
            } else {
                Optional<CouponService> optional = couponServiceRepository.findById(id);
                //判断数据库中是否有该对象
                if (!optional.isPresent()) {
                    logger.error("修改失败，原因：" + CouponService.class + "中不存在id为 " + id + " 的对象");
                    return ResultJsonObject.getErrorResult(null);
                }
                CouponService source = optional.get();
                //将目标对象（projectType）中的null值，用源对象中的值替换
                UpdateTool.copyNullProperties(source, couponService);
            }
            //执行保存/修改
            return ResultJsonObject.getDefaultResult(couponServiceRepository.saveAndFlush(couponService));
        } catch (Exception e) {
            return ResultJsonObject.getErrorResult(null);
        }
    }

    /**
     * 验证项目是否重复
     * true：重复；false：不重复
     *
     * @param couponService
     * @return
     */
    private boolean checkRepeatName(CouponService couponService) {
        List<CouponService> couponServiceList;
        if (null != couponService.getId()) {
            couponServiceList = couponServiceRepository.checkRepeatName(couponService.getId(), couponService.getName(), couponService.getStoreId());
        } else {
            couponServiceList = couponServiceRepository.checkRepeatName(couponService.getName(), couponService.getStoreId());
        }
        return couponServiceList.size() != 0;
    }

    /**
     * 获取抵用券类详情
     *
     * @param id
     * @return
     */
    public ResultJsonObject getDetail(String id) {
        return ResultJsonObject.getDefaultResult(couponServiceRepository.findById(id));
    }


    /**
     * 查询抵用券类列表
     *
     * @param storeId
     * @param currentPage
     * @param pageSize
     * @return
     */
    public PaginationRJO list(String storeId, Integer currentPage, Integer pageSize, String name) {
        logger.debug("storeId:" + storeId);
        Page<CouponService> couponServicePage = couponServiceRepository.findAllByDelStatusAndStoreIdAndNameContaining(Constants.DelStatus.NORMAL.isValue(), storeId, name, PageableTools.basicPage(currentPage, pageSize));
        return PaginationRJO.of(couponServicePage);
    }

    /**
     * 删除操作（软删除）
     *
     * @param id
     * @return
     */
    @Transactional
    public ResultJsonObject delete(String id) {
        try {
            int result = couponServiceRepository.delById(id);
            if (result != 1) {
                return ResultJsonObject.getErrorResult(id, "删除失败," + RESULT_DATA_NONE);
            }
        } catch (Exception e) {
            return ResultJsonObject.getErrorResult(id, "删除失败，删除操作出现异常");
        }
        return ResultJsonObject.getDefaultResult(id, "删除成功");
    }


    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    public ResultJsonObject delByIds(String ids) {
        if (StringUtils.isEmpty(ids)) {
            return ResultJsonObject.getErrorResult(null, "删除失败：ids" + ResultCode.PARAM_NOT_COMPLETE.message());
        }
        String[] idsList = ids.split(",");
        for (String id : idsList) {
            couponServiceRepository.delById(id);
        }
        return ResultJsonObject.getDefaultResult(null);
    }

    /**
     * 上架
     *
     * @param id
     * @return
     */
    @Transactional
    public ResultJsonObject upperShelf(String id) {
        try {
            int result = couponServiceRepository.uppById(id);
            if (result != 1) {
                return ResultJsonObject.getErrorResult(id, "上架失败," + RESULT_DATA_NONE);
            }
        } catch (Exception e) {
            return ResultJsonObject.getErrorResult(id, "上架失败，上架操作出现异常");
        }
        return ResultJsonObject.getDefaultResult(id, "上架成功");
    }

    /**
     * 下架
     *
     * @param id
     * @return
     */
    @Transactional
    public ResultJsonObject lowerShelf(String id) {
        try {
            int result = couponServiceRepository.lowById(id);
            if (result != 1) {
                return ResultJsonObject.getErrorResult(id, "下架失败," + RESULT_DATA_NONE);
            }
        } catch (Exception e) {
            return ResultJsonObject.getErrorResult(id, "下架失败，下架操作出现异常");
        }
        return ResultJsonObject.getDefaultResult(id, "下架成功");
    }

    /**
     * 查找上架在售的抵用券
     *
     * @param storeId
     * @return
     */
    public List<CouponInfo> findOnSaleCoupons(String storeId) throws ArgumentMissingException {
//        return couponServiceRepository.findByStoreIdAndDelStatusAndBookOnline(storeId, Constants.DelStatus.NORMAL.isValue(), true);
        if (StringUtils.isEmpty(storeId)) {
            throw new ArgumentMissingException("参数storeId值位空");
        }

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT cs.id, cs.price, cs.`name`, p.price AS originalPrice FROM couponService cs LEFT JOIN project p ON p.id = cs.projectId WHERE cs.storeId = ").append(storeId).append(" AND cs.bookOnline = 1 AND cs.delStatus = 0 ORDER BY cs.createTime DESC ");

        EntityManager em = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
        Query nativeQuery = em.createNativeQuery(sql.toString());
        nativeQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(CouponInfo.class));
        @SuppressWarnings({"unused", "unchecked"})
        List<CouponInfo> couponInfos = nativeQuery.getResultList();

        //关闭em
        em.close();

        return couponInfos;
    }

    //
    public String generateCouponOrder(String clientId, String couponServiceId) throws Exception {
        if (StringUtils.isEmpty(clientId)) {
            throw new ArgumentMissingException("参数clientId为空，生成抵用券购买订单失败！");
        }
        if (StringUtils.isEmpty(couponServiceId)) {
            throw new ArgumentMissingException("参数couponServiceId为空，生成抵用券购买订单失败！");
        }

        Client client = clientService.findById(clientId);
        if (null == client) {
            throw new ObjectNotFoundException("用户信息查找失败，无法生成购买订单，请稍后再试或联系客服。");
        }

        CouponService couponServiceObject = couponServiceRepository.findById(couponServiceId).orElse(null);
        if (null == couponServiceObject) {
            throw new ObjectNotFoundException("抵用券信息查找失败，无法生成购买订单，请稍后再试或联系客服。");
        }

        String clientStoreId = client.getStoreId();
        String couponServiceStoreId = couponServiceObject.getStoreId();
        if (null != clientStoreId && null != couponServiceStoreId && !clientStoreId.equals(couponServiceStoreId)) {
            logger.error("clientStoreId:" + clientStoreId);
            logger.error("couponServiceStoreId:" + couponServiceStoreId);
            throw new Exception("用户信息与抵用券信息所属门店不同，无法生成购买订单，请核实或联系客服。");
        }

        double price = RoundTool.round(couponServiceObject.getPrice(), 2, BigDecimal.ROUND_HALF_UP);

        //生成coupon对象（未支付前是不可用的：delStatus是1）
        Coupon coupon = new Coupon();
        coupon.setDelStatus(Constants.DelStatus.DELETE.isValue());
        coupon.setCreateTime(new Timestamp(System.currentTimeMillis()));
        coupon.setClientId(clientId);
        coupon.setStatus(Constants.CouponStatus.NOT_USE.getValue());
        coupon.setName(couponServiceObject.getName());
        coupon.setProjectId(couponServiceObject.getProjectId());
        coupon.setStoreId(couponServiceStoreId);
        coupon.setCouponServiceId(couponServiceId);
        coupon.setProjectId(couponServiceObject.getProjectId());
        coupon.setPrice(price);
        coupon.setContent(couponServiceObject.getContent());

        Coupon couponRes = couponRepository.saveAndFlush(coupon);
        if (null == couponRes) {
            throw new Exception("抵用券数据生成异常，无法生成购买订单，请核实或联系客服。");
        }

        //生成订单
        ConsumerOrder consumerOrder = consumerOrderService.generateOrderForBuyCardOrCoupon(client, price, couponRes.getId());

        return consumerOrder.getId();
    }
}
