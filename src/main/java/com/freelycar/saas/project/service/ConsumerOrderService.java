package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.*;
import com.freelycar.saas.exception.*;
import com.freelycar.saas.project.entity.*;
import com.freelycar.saas.project.model.*;
import com.freelycar.saas.project.repository.*;
import com.freelycar.saas.util.*;
import com.freelycar.saas.wechat.model.BaseOrderInfo;
import com.freelycar.saas.wechat.model.FinishOrderInfo;
import com.freelycar.saas.wechat.model.ReservationOrderInfo;
import com.freelycar.saas.wxutils.WechatTemplateMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

/**
 * @author tangwei - Toby
 * @date 2018-12-28
 * @email toby911115@gmail.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ConsumerOrderService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;

    @Autowired
    private ConsumerOrderRepository consumerOrderRepository;

    @Autowired
    private ConsumerProjectInfoService consumerProjectInfoService;

    @Autowired
    private AutoPartsService autoPartsService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CardService cardService;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private CarService carService;

    @Autowired
    private OrderIDGenerator orderIDGenerator;

    @Autowired
    private StaffService staffService;

    @Autowired
    private WxUserInfoService wxUserInfoService;

    @Autowired
    private DoorRepository doorRepository;

    @Autowired
    private DoorService doorService;

    @Autowired
    private CardServiceRepository cardServiceRepository;

    @Autowired
    private CouponServiceRepository couponServiceRepository;

    /**
     * 保存和修改
     *
     * @param consumerOrder
     * @return
     */
    public ConsumerOrder saveOrUpdate(ConsumerOrder consumerOrder) throws Exception {
        if (null == consumerOrder) {
            return null;
        }
        String id = consumerOrder.getId();
        if (StringUtils.isEmpty(id)) {
            //订单号生成规则：订单类型编号（1位）+ 门店（3位）+ 日期（6位）+ 每日递增（4位）
            consumerOrder.setId(orderIDGenerator.generate(consumerOrder.getStoreId(), consumerOrder.getOrderType()));
            consumerOrder.setDelStatus(Constants.DelStatus.NORMAL.isValue());
            consumerOrder.setCreateTime(new Timestamp(System.currentTimeMillis()));
            return consumerOrderRepository.saveAndFlush(consumerOrder);
        }
        return this.updateOrder(consumerOrder);
    }

    /**
     * 更新order信息
     *
     * @param consumerOrder
     * @return
     */
    public ConsumerOrder updateOrder(ConsumerOrder consumerOrder) {
        String id = consumerOrder.getId();
        Optional<ConsumerOrder> consumerOrderOptional = consumerOrderRepository.findById(id);
        if (!consumerOrderOptional.isPresent()) {
            return null;
        }
        ConsumerOrder source = consumerOrderOptional.get();
        UpdateTool.copyNullProperties(source, consumerOrder);
        return consumerOrderRepository.saveAndFlush(consumerOrder);
    }


    /**
     * 快速开单
     *
     * @param orderObject
     * @return
     */
    public ResultJsonObject handleOrder(OrderObject orderObject) {
        ConsumerOrder consumerOrder = orderObject.getConsumerOrder();
        List<ConsumerProjectInfo> consumerProjectInfos = orderObject.getConsumerProjectInfos();
        List<AutoParts> autoParts = orderObject.getAutoParts();

        //设置order的额外信息
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        consumerOrder.setOrderType(Constants.OrderType.SERVICE.getValue());
        consumerOrder.setPayState(Constants.PayState.NOT_PAY.getValue());
        //快速开单时订单状态直接为接车状态（不需要预约）
        consumerOrder.setState(Constants.OrderState.RECEIVE_CAR.getValue());
        consumerOrder.setPickTime(currentTime);
        //应付金额等于整单金额
        consumerOrder.setActualPrice(consumerOrder.getTotalPrice());

        ConsumerOrder consumerOrderRes;
        try {
            consumerOrderRes = this.saveOrUpdate(consumerOrder);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return ResultJsonObject.getErrorResult(null, "开单失败！" + e.getMessage());
        }
        if (null == consumerOrderRes) {
            return ResultJsonObject.getErrorResult(null, "开单失败！保存订单信息失败。如有疑问，请联系管理员！");
        }

        String orderId = consumerOrder.getId();

        //保存订单项目信息
        if (null != consumerProjectInfos && !consumerProjectInfos.isEmpty()) {
            for (ConsumerProjectInfo consumerProjectInfo : consumerProjectInfos) {
                consumerProjectInfo.setConsumerOrderId(orderId);
                consumerProjectInfoService.saveOrUpdate(consumerProjectInfo);
            }
        }

        //保存项目相关配件
        if (null != autoParts && !autoParts.isEmpty()) {
            for (AutoParts autoPart : autoParts) {
                autoPart.setConsumerOrderId(orderId);
                autoPartsService.saveOrUpdate(autoPart);
            }
        }

        return ResultJsonObject.getDefaultResult(consumerOrderRes.getId(), "订单生成成功！");
    }

    /**
     * 根据clientId查找所有有效订单
     *
     * @param clientId
     * @return
     */
    public List<ConsumerOrder> findAllEffectiveOrdersByClientId(String clientId) {
        return consumerOrderRepository.findAllByClientIdAndDelStatusOrderByCreateTimeDesc(clientId, Constants.DelStatus.NORMAL.isValue());
    }

    /**
     * 查询某人的某类类型的所有订单
     *
     * @param clientId
     * @param type
     * @return
     */
    public List<ConsumerOrder> findAllOrdersByTypeAndClientId(String clientId, String type) {
        if (Constants.OrderType.SERVICE.getName().equalsIgnoreCase(type)) {
            return consumerOrderRepository.findAllByClientIdAndDelStatusAndOrderTypeOrderByCreateTimeDesc(clientId, Constants.DelStatus.NORMAL.isValue(), Constants.OrderType.SERVICE.getValue());
        }
        if (Constants.OrderType.ARK.getName().equalsIgnoreCase(type)) {
            return consumerOrderRepository.findAllByClientIdAndDelStatusAndOrderTypeOrderByCreateTimeDesc(clientId, Constants.DelStatus.NORMAL.isValue(), Constants.OrderType.ARK.getValue());
        }
        if (Constants.OrderType.CARD.getName().equalsIgnoreCase(type)) {
            return consumerOrderRepository.findAllByClientIdAndDelStatusAndOrderTypeOrderByCreateTimeDesc(clientId, Constants.DelStatus.NORMAL.isValue(), Constants.OrderType.CARD.getValue());
        }
        return null;
    }

    public List<BaseOrderInfo> findAllOrdersByClientId(String clientId, String type) {
        StringBuilder sql = new StringBuilder();
        if (Constants.OrderType.SERVICE.getName().equalsIgnoreCase(type)) {
            sql.append(" SELECT co.id, co.licensePlate AS licensePlate, co.carBrand AS carBrand, co.carType AS carType, co.clientName AS clientName, ( SELECT GROUP_CONCAT( cpi.projectName ) FROM consumerProjectInfo cpi WHERE cpi.consumerOrderId = co.id AND cpi.delStatus=0 GROUP BY cpi.consumerOrderId ) AS projectNames, co.createTime AS createTime, co.pickTime AS pickTime, co.finishTime AS finishTime, co.state, co.actualPrice as actualPrice, co.totalPrice as totalPrice, co.payState AS payState FROM consumerOrder co WHERE co.delStatus = 0 ");
            sql.append(" AND co.orderType < 3 ");
        } else if (Constants.OrderType.CARD.getName().equalsIgnoreCase(type)) {
            sql.append(" SELECT co.id, co.licensePlate AS licensePlate, co.carBrand AS carBrand, co.carType AS carType, co.clientName AS clientName, ( SELECT GROUP_CONCAT( c.`name` ) FROM card c WHERE c.id = co.cardOrCouponId ) AS cardName, ( SELECT GROUP_CONCAT( cp.`name` ) FROM coupon cp WHERE cp.id = co.cardOrCouponId ) AS couponName, co.createTime AS createTime, co.pickTime AS pickTime, co.finishTime AS finishTime, co.state, co.actualPrice AS actualPrice, co.totalPrice AS totalPrice, co.payState AS payState FROM consumerOrder co WHERE co.delStatus = 0 AND co.orderType = 3 ");
        } else {
            return null;
        }

        sql.append(" AND co.clientId = '").append(clientId).append("' ORDER BY co.createTime DESC ");

        EntityManager em = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
        Query nativeQuery = em.createNativeQuery(sql.toString());
        nativeQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(BaseOrderInfo.class));
        @SuppressWarnings({"unused", "unchecked"})
        List<BaseOrderInfo> baseOrderInfos = nativeQuery.getResultList();

        //关闭entityManagerFactory
        em.close();

        return baseOrderInfos;

    }

    /**
     * 查询可接车的智能柜预约
     *
     * @param licensePlate
     * @return
     */
    public List<ReservationOrderInfo> listReservationOrders(String licensePlate, String storeId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT co.id, co.licensePlate as licensePlate, co.carBrand as carBrand, co.carType as carType, co.clientName AS clientName, ( SELECT GROUP_CONCAT( cpi.projectName ) FROM consumerProjectInfo cpi WHERE cpi.consumerOrderId = co.id GROUP BY cpi.consumerOrderId ) AS projectNames, co.createTime AS createTime, co.parkingLocation AS parkingLocation, d.arkSn AS arkSn, d.doorSn AS doorSn, concat( ( SELECT ark.`name` FROM ark WHERE ark.id = d.arkId ), '-', d.doorSn, '门' ) AS keyLocation FROM door d LEFT JOIN consumerOrder co ON co.id = d.orderId WHERE co.state = 0 ")
                .append(" AND co.storeId = ").append(storeId);
        if (StringUtils.hasText(licensePlate)) {
            sql.append(" and co.licensePlate like '%").append(licensePlate).append("%' ");
        }
        sql.append(" ORDER BY co.createTime ASC");

        EntityManager em = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
        Query nativeQuery = em.createNativeQuery(sql.toString());
        nativeQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(ReservationOrderInfo.class));
        @SuppressWarnings({"unused", "unchecked"})
        List<ReservationOrderInfo> reservationOrderInfos = nativeQuery.getResultList();

        //关闭em
        em.close();

        return reservationOrderInfos;
    }


    /**
     * 查询可完工的智能柜订单
     *
     * @param licensePlate
     * @param storeId
     * @return
     */
    public List<FinishOrderInfo> listServicingOrders(String licensePlate, String storeId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT co.id, co.clientName AS clientName, co.licensePlate as licensePlate, co.carBrand as carBrand, co.carType as carType, ( SELECT GROUP_CONCAT( cpi.projectName ) FROM consumerProjectInfo cpi WHERE cpi.consumerOrderId = co.id GROUP BY cpi.consumerOrderId ) projectNames, co.pickTime as pickTime FROM consumerOrder co WHERE co.delStatus = 0 AND co.orderType = 2 AND co.state = 1 ")
                .append(" AND co.storeId = ").append(storeId);
        if (StringUtils.hasText(licensePlate)) {
            sql.append(" AND co.licensePlate LIKE '%").append(licensePlate).append("%' ");
        }
        sql.append(" ORDER BY co.pickTime ASC ");
        EntityManager em = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
        Query nativeQuery = em.createNativeQuery(sql.toString());
        nativeQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(FinishOrderInfo.class));
        @SuppressWarnings({"unused", "unchecked"})
        List<FinishOrderInfo> finishOrderInfo = nativeQuery.getResultList();

        //关闭em
        em.close();

        return finishOrderInfo;
    }

    /**
     * 获取订单详情的数据（用于详情页展示和结算中心展示）
     *
     * @param id
     * @return
     */
    public ResultJsonObject getOrderObjectDetail(String id) {
        if (StringUtils.isEmpty(id)) {
            return ResultJsonObject.getErrorResult(null, "参数id为NULL");
        }

        OrderObject orderObject = new OrderObject();

        Optional<ConsumerOrder> optionalConsumerOrder = consumerOrderRepository.findById(id);
        if (!optionalConsumerOrder.isPresent()) {
            return ResultJsonObject.getErrorResult(null, "未找到id为：" + id + " 的订单数据");
        }

        ConsumerOrder consumerOrder = optionalConsumerOrder.get();

        //获取服务项目
        List<ConsumerProjectInfo> consumerProjectInfos = consumerProjectInfoService.getAllProjectInfoByOrderId(id);

        //获取配件
        List<AutoParts> autoPartsList = autoPartsService.getAllAutoPartsByOrderId(id);


        //获取相关的卡信息或券信息
        if (Constants.OrderIdSn.CARD.getName().equals(id.substring(0, 1))) {
            String cardOrCouponId = consumerOrder.getCardOrCouponId();
            if (StringUtils.hasText(cardOrCouponId)) {
                Card card = cardRepository.findById(cardOrCouponId).orElse(null);
                Coupon coupon = couponRepository.findById(cardOrCouponId).orElse(null);
                if (null != card) {
                    orderObject.setCard(card);
                }
                if (null != coupon) {
                    orderObject.setCoupon(coupon);
                }
            }
        }

        orderObject.setConsumerOrder(consumerOrder);
        orderObject.setConsumerProjectInfos(consumerProjectInfos);
        orderObject.setAutoParts(autoPartsList);

        return ResultJsonObject.getDefaultResult(orderObject);
    }

    /**
     * 结算（需要处理卡券抵扣等业务逻辑）
     *
     * @param payOrder
     * @return
     */
    public ResultJsonObject payment(PayOrder payOrder) {
        ConsumerOrder consumerOrder = payOrder.getConsumerOrder();
        if (null == consumerOrder) {
            return ResultJsonObject.getErrorResult(null, "consumerOrder对象为NULL！");
        }

        String orderId = consumerOrder.getId();

        consumerOrder.setPayState(Constants.PayState.FINISH_PAY.getValue());
        consumerOrder.setState(Constants.OrderState.HAND_OVER.getValue());



        /*
        处理抵用券的结算
         */
        //查询所有关联的抵用券，并将所有orderId和status初始化
        couponService.initCouponByOrderId(orderId);

        //将抵用券设置为已使用
        List<Coupon> coupons = payOrder.getUseCoupons();
        for (Coupon coupon : coupons) {
            coupon.setOrderId(orderId);
            coupon.setStatus(Constants.CouponStatus.BEEN_USED.getValue());
            couponService.saveOrUpdate(coupon);
        }

        /*
        结算方式一
         */
        Integer firstPayMethod = consumerOrder.getFirstPayMethod();
        Double firstActualPrice = consumerOrder.getFirstActualPrice();
        String firstCardId = consumerOrder.getFirstCardId();

        //如果支付方式为空，不结算支付方式一
        if (null != firstPayMethod) {
            //如果支付一是卡支付，进行余额扣除
            if (Constants.PayMethod.CARD.getCode().intValue() == firstPayMethod) {
                cardService.cardSettlement(firstCardId, firstActualPrice.floatValue());
            }
            //如果是其他支付方式，直接保存即可
        }


        /*
        结算方式二
         */
        Integer secondPayMethod = consumerOrder.getSecondPayMethod();
        Double secondActualPrice = consumerOrder.getSecondActualPrice();
        String secondCardId = consumerOrder.getSecondCardId();

        //如果支付方式为空，不结算支付方式一
        if (null != secondPayMethod) {
            //如果支付一是卡支付，进行余额扣除
            if (Constants.PayMethod.CARD.getCode().intValue() == secondPayMethod) {
                cardService.cardSettlement(secondCardId, secondActualPrice.floatValue());
            }
            //如果是其他支付方式，直接保存即可
        }

        //判断实付总金额是否为两种支付方式的总和
        if (!this.isTotalActualPriceRight(consumerOrder)) {
            consumerOrder.setActualPrice(this.sumActualPrice(consumerOrder));
        }

        //保存订单信息（结算）

        this.updateOrder(consumerOrder);

        // 处理其他逻辑，比如推送消息和消费金额叠加之类的
        clientService.updateClientAcount(consumerOrder.getClientId(), consumerOrder.getActualPrice());

        return ResultJsonObject.getDefaultResult(consumerOrder.getId(), "结算成功");
    }

    /**
     * 挂单（本质是保存，但是需要考虑抵用券选用的情况）
     *
     * @param payOrder
     * @return
     */
    public ResultJsonObject pendingOrder(PayOrder payOrder) {
        ConsumerOrder consumerOrder = payOrder.getConsumerOrder();
        if (null == consumerOrder) {
            return ResultJsonObject.getErrorResult(null, "consumerOrder对象为NULL！");
        }

        String orderId = consumerOrder.getId();

        consumerOrder = this.updateOrder(consumerOrder);
        if (null == consumerOrder) {
            return ResultJsonObject.getErrorResult(null, "挂单操作失败：数据保存错误！");
        }


        //查询所有关联的抵用券，并将所有orderId和status初始化
        couponService.initCouponByOrderId(orderId);

        //将抵用券设置为挂单
        List<Coupon> coupons = payOrder.getUseCoupons();
        for (Coupon coupon : coupons) {
            coupon.setOrderId(orderId);
            coupon.setStatus(Constants.CouponStatus.KEEP.getValue());
            couponService.saveOrUpdate(coupon);
        }

        return ResultJsonObject.getDefaultResult(orderId);
    }

    /**
     * 判断总实付金额是否等于两种支付方式总和
     *
     * @param consumerOrder
     * @return
     */
    private boolean isTotalActualPriceRight(ConsumerOrder consumerOrder) {
        Double firstActualPrice = consumerOrder.getFirstActualPrice() == null ? 0 : consumerOrder.getFirstActualPrice();
        Double secondActualPrice = consumerOrder.getSecondActualPrice() == null ? 0 : consumerOrder.getSecondActualPrice();
        Double actualPrice = consumerOrder.getActualPrice();
        return null != actualPrice && firstActualPrice + secondActualPrice == actualPrice;
    }

    /**
     * 计算实付金额
     *
     * @param consumerOrder
     * @return
     */
    private double sumActualPrice(ConsumerOrder consumerOrder) {
        Double firstActualPrice = consumerOrder.getFirstActualPrice() == null ? 0 : consumerOrder.getFirstActualPrice();
        Double secondActualPrice = consumerOrder.getSecondActualPrice() == null ? 0 : consumerOrder.getSecondActualPrice();
        return firstActualPrice + secondActualPrice;
    }


    /**
     * 单据列表条件查询（作废）
     *
     * @param params
     * @return
     */
    @Deprecated
    public ResultJsonObject list(String storeId, Integer currentPage, Integer pageSize, OrderListParam params) {
        String orderId = params.getOrderId();
        String licensePlate = params.getLicensePlate();

        // 如何查出订单类型，还得考虑一下
        String projectId = params.getProjectId();

        Integer orderState = params.getOrderState();
        Integer orderType = params.getOrderType();
        Integer payState = params.getPayState();

        Integer dateType = params.getDateType();
        String startTime = params.getStartTime();
        String endTime = params.getEndTime();

        Page<ConsumerOrder> resultPage;
        Specification<ConsumerOrder> querySpecification = (Specification<ConsumerOrder>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("delStatus"), 0));
            predicates.add(criteriaBuilder.equal(root.get("storeId"), storeId));

            //订单号模糊查询
            if (StringUtils.hasText(orderId)) {
                predicates.add(criteriaBuilder.like(root.get("id"), "%" + orderId + "%"));
            }
            //车牌号模糊查询
            if (StringUtils.hasText(licensePlate)) {
                predicates.add(criteriaBuilder.like(root.get("licensePlate"), "%" + licensePlate + "%"));
            }
            //订单状态条件查询
            if (null != orderState) {
                predicates.add(criteriaBuilder.equal(root.get("state"), orderState));
            }
            //订单类型条件查询（如果没传，默认是查询出非办卡类的）
            if (null != orderType) {
                predicates.add(criteriaBuilder.equal(root.get("orderType"), orderType));
            } else {
                predicates.add(criteriaBuilder.notEqual(root.get("orderType"), Constants.OrderType.CARD.getValue()));
            }
            //支付状态条件查询
            if (null != payState) {
                predicates.add(criteriaBuilder.equal(root.get("payState"), payState));
            }
            //时间范围条件查询
            if (null != dateType) {
                Date start = null;
                Date end = null;
                if (StringUtils.hasText(startTime)) {
                    try {
                        start = DateUtils.parseDate(startTime + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (StringUtils.hasText(endTime)) {
                    try {
                        end = DateUtils.parseDate(endTime + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if (Constants.DateType.ORDER.getValue().intValue() == dateType) {
                    if (null != start) {
                        predicates.add(criteriaBuilder.greaterThan(root.get("createTime"), start));
                    }
                    if (null != end) {
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createTime"), end));
                    }
                }

                if (Constants.DateType.PICK.getValue().intValue() == dateType) {
                    if (null != start) {
                        predicates.add(criteriaBuilder.greaterThan(root.get("pickTime"), start));
                    }
                    if (null != end) {
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("pickTime"), end));
                    }
                }

                if (Constants.DateType.FINISH.getValue().intValue() == dateType) {
                    if (null != start) {
                        predicates.add(criteriaBuilder.greaterThan(root.get("finishTime"), start));
                    }
                    if (null != end) {
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("finishTime"), end));
                    }
                }

                if (Constants.DateType.DELIVER.getValue().intValue() == dateType) {
                    if (null != start) {
                        predicates.add(criteriaBuilder.greaterThan(root.get("deliverTime"), start));
                    }
                    if (null != end) {
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("deliverTime"), end));
                    }
                }
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        resultPage = consumerOrderRepository.findAll(querySpecification, PageableTools.basicPage(currentPage, pageSize));
        return ResultJsonObject.getDefaultResult(PaginationRJO.of(resultPage));
    }

    /**
     * 单据列表条件查询
     * （替代原方法）
     *
     * @param storeId
     * @param currentPage
     * @param pageSize
     * @param params
     * @return
     */
    public ResultJsonObject listSql(String storeId, Integer currentPage, Integer pageSize, OrderListParam params, boolean export) {
        String orderId = params.getOrderId();
        String licensePlate = params.getLicensePlate();

        String projectId = params.getProjectId();

        Integer orderState = params.getOrderState();
        Integer orderType = params.getOrderType();
        Integer payState = params.getPayState();

        Integer dateType = params.getDateType();
        String startTime = params.getStartTime();
        String endTime = params.getEndTime();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT co.id AS id, co.createTime,co.licensePlate,co.orderType,co.totalPrice,co.actualPrice,co.state,co.payState,co.phone, group_concat(pt.name) AS project,co.parkingLocation,co.pickTime,co.finishTime,co.deliverTime FROM consumerorder co JOIN consumerprojectinfo cpi ON cpi.consumerOrderId = co.id LEFT JOIN project p ON p.id = cpi.projectId LEFT JOIN projectType pt on pt.id=p.projectTypeId WHERE co.delStatus = 0 ");
        sql.append(" AND co.storeId = '").append(storeId).append("' ");

        //订单号模糊查询
        if (StringUtils.hasText(orderId)) {
            sql.append(" AND co.id like '%").append(orderId).append("%' ");
        }
        //车牌号模糊查询
        if (StringUtils.hasText(licensePlate)) {
            sql.append(" AND co.licensePlate like '%").append(licensePlate).append("%' ");
        }
        //项目类型查询
        if (StringUtils.hasText(projectId)) {
            sql.append(" AND p.projectTypeId = '").append(projectId).append("' ");
        }

        //订单状态条件查询
        if (null != orderState) {
            sql.append(" AND co.orderState = ").append(orderState).append(" ");
        }
        //订单类型条件查询（如果没传，默认是查询出非办卡类的）
        if (null != orderType) {
            sql.append(" AND co.orderType=").append(orderType).append(" ");
        } else {
            sql.append(" AND co.orderType !=").append(Constants.OrderType.CARD.getValue()).append(" ");
        }
        //支付状态条件查询
        if (null != payState) {
            sql.append(" AND co.payState=").append(payState).append(" ");
        }
        //时间范围条件查询
        if (null != dateType) {
            String start = null;
            String end = null;

            if (StringUtils.hasText(startTime)) {
                start = startTime + " 00:00:00";
            }
            if (StringUtils.hasText(endTime)) {
                end = endTime + " 23:59:59";
            }

            if (Constants.DateType.ORDER.getValue().intValue() == dateType) {
                if (StringUtils.hasText(start)) {
                    sql.append(" AND co.createTime >= '").append(start).append("' ");
                }
                if (StringUtils.hasText(end)) {
                    sql.append(" AND co.createTime <= '").append(end).append("' ");
                }
            }

            if (Constants.DateType.PICK.getValue().intValue() == dateType) {
                if (StringUtils.hasText(start)) {
                    sql.append(" AND co.pickTime >= '").append(start).append("' ");
                }
                if (StringUtils.hasText(end)) {
                    sql.append(" AND co.pickTime <= '").append(end).append("' ");
                }
            }

            if (Constants.DateType.FINISH.getValue().intValue() == dateType) {
                if (StringUtils.hasText(start)) {
                    sql.append(" AND co.finishTime >= '").append(start).append("' ");
                }
                if (StringUtils.hasText(end)) {
                    sql.append(" AND co.finishTime <= '").append(end).append("' ");
                }
            }

            if (Constants.DateType.DELIVER.getValue().intValue() == dateType) {
                if (StringUtils.hasText(start)) {
                    sql.append(" AND co.deliverTime >= '").append(start).append("' ");
                }
                if (StringUtils.hasText(end)) {
                    sql.append(" AND co.deliverTime <= '").append(end).append("' ");
                }
            }
        }
        sql.append(" GROUP BY co.id ORDER BY co.createTime DESC");

        EntityManager em = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
        Query nativeQuery = em.createNativeQuery(sql.toString());
        nativeQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(CustomerOrderListObject.class));

        // 如果是导出EXCEL方法，就不分页
        if (export) {
            @SuppressWarnings({"unused", "unchecked"})
            List<CustomerOrderListObject> customerOrderListObjects = nativeQuery.getResultList();

            //关闭em
            em.close();

            return ResultJsonObject.getDefaultResult(customerOrderListObjects);
        } else {
            Pageable pageable = PageableTools.basicPage(currentPage, pageSize);
            int total = nativeQuery.getResultList().size();
            @SuppressWarnings({"unused", "unchecked"})
            List<CustomerOrderListObject> customerOrderListObjects = nativeQuery.setFirstResult(MySQLPageTool.getStartPosition(currentPage, pageSize)).setMaxResults(pageSize).getResultList();

            //关闭em
            em.close();
            @SuppressWarnings("unchecked")
            Page<CustomerOrderListObject> page = new PageImpl(customerOrderListObjects, pageable, total);


            return ResultJsonObject.getDefaultResult(PaginationRJO.of(page));
        }
    }


    /**
     * 完工
     *
     * @param consumerOrder
     * @return
     */
    public ResultJsonObject serviceFinish(ConsumerOrder consumerOrder) {
        if (StringUtils.isEmpty(consumerOrder)) {
            return ResultJsonObject.getErrorResult(consumerOrder, "参数consumerOrder为NULL，完工操作失败");
        }
        String orderId = consumerOrder.getId();
        Timestamp finishTime = consumerOrder.getFinishTime() == null ? new Timestamp(System.currentTimeMillis()) : consumerOrder.getFinishTime();
        ConsumerOrder source = consumerOrderRepository.getOne(orderId);
        source.setFinishTime(finishTime);
        source.setParkingLocation(consumerOrder.getParkingLocation());
        source.setState(Constants.OrderState.SERVICE_FINISH.getValue());

        this.updateOrder(source);
        return ResultJsonObject.getDefaultResult(orderId);
    }


    /**
     * 交车
     *
     * @param consumerOrder
     * @return
     */
    public ResultJsonObject handOver(ConsumerOrder consumerOrder) {
        if (StringUtils.isEmpty(consumerOrder)) {
            return ResultJsonObject.getErrorResult(consumerOrder, "参数consumerOrder为NULL，交车操作失败");
        }
        String orderId = consumerOrder.getId();
        Timestamp deliverTime = consumerOrder.getDeliverTime() == null ? new Timestamp(System.currentTimeMillis()) : consumerOrder.getDeliverTime();
        ConsumerOrder source = consumerOrderRepository.getOne(orderId);
        source.setDeliverTime(deliverTime);
        source.setState(Constants.OrderState.HAND_OVER.getValue());

        this.updateOrder(source);
        return ResultJsonObject.getDefaultResult(orderId);
    }


    /**
     * 获取当前用户，当前活跃的智能柜订单
     *
     * @param clientId
     * @return
     */
    public ResultJsonObject getActiveOrder(String clientId) {
        if (StringUtils.isEmpty(clientId)) {
            return ResultJsonObject.getErrorResult(clientId, "参数clientId为空值");
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT co.id, co.licensePlate AS licensePlate, co.carBrand AS carBrand, co.carType AS carType, co.clientName AS clientName, ( SELECT GROUP_CONCAT( cpi.projectName ) FROM consumerProjectInfo cpi WHERE cpi.consumerOrderId = co.id GROUP BY cpi.consumerOrderId ) AS projectNames, co.createTime AS createTime, co.pickTime AS pickTime, co.finishTime AS finishTime, co.state, co.actualPrice as actualPrice, co.totalPrice as totalPrice FROM consumerOrder co WHERE co.orderType=2 AND co.delStatus = 0 AND co.state < 3 ")
                .append(" AND co.clientId = '").append(clientId).append("' ORDER BY co.createTime DESC ");

        EntityManager em = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
        Query nativeQuery = em.createNativeQuery(sql.toString());
        nativeQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(BaseOrderInfo.class));
        @SuppressWarnings({"unused", "unchecked"})
        List<BaseOrderInfo> activeArkOrderInfos = nativeQuery.getResultList();

        //关闭em
        em.close();

        if (null != activeArkOrderInfos && !activeArkOrderInfos.isEmpty()) {
            return ResultJsonObject.getDefaultResult(activeArkOrderInfos.get(0));
        }
        return ResultJsonObject.getDefaultResult(null);
    }

    /**
     * 智能柜开单
     *
     * @param orderObject
     * @return
     */
    public ResultJsonObject arkHandleOrder(OrderObject orderObject) throws Exception {
        String arkSn = orderObject.getArkSn();
        //获取提交过来的数据
        ConsumerOrder consumerOrder = orderObject.getConsumerOrder();
        List<ConsumerProjectInfo> consumerProjectInfos = orderObject.getConsumerProjectInfos();

        if (StringUtils.isEmpty(arkSn)) {
            logger.error("智能柜开单失败：参数中的arkSn对象为空，无法分配智能柜");
            return ResultJsonObject.getErrorResult(null, "智能柜开单失败：参数中的arkSn对象为空，无法分配智能柜");
        }

        if (null == consumerOrder) {
            logger.error("智能柜开单失败：参数中的consumerOrder对象为空");
            return ResultJsonObject.getErrorResult(null, "智能柜开单失败：参数中的consumerOrder对象为空");
        }

        String carId = consumerOrder.getCarId();
        String clientId = consumerOrder.getClientId();

        //获取车辆信息
        Car carInfo = carService.findById(carId);
        if (null == carInfo) {
            logger.error("智能柜开单失败：未找到对应的车辆信息 " + carId);
            return ResultJsonObject.getErrorResult(null, "智能柜开单失败：未找到对应的车辆信息");
        }

        //获取客户信息
        Client clientInfo = clientService.findById(clientId);
        if (null == clientInfo) {
            logger.error("智能柜开单失败：未找到对应的车主信息 " + clientId);
            return ResultJsonObject.getErrorResult(null, "智能柜开单失败：未找到对应的车主信息");
        }

        //设置order中的车辆信息
        consumerOrder.setCarId(carId);
        consumerOrder.setLicensePlate(carInfo.getLicensePlate());
        consumerOrder.setCarBrand(carInfo.getCarBrand());
        consumerOrder.setCarType(carInfo.getCarType());
        consumerOrder.setLastMiles(carInfo.getLastMiles());
        consumerOrder.setMiles(carInfo.getMiles());

        //设置车主信息
        consumerOrder.setClientId(clientId);
        consumerOrder.setClientName(clientInfo.getTrueName());
        consumerOrder.setPhone(clientInfo.getPhone());
        consumerOrder.setIsMember(clientInfo.getMember());
        consumerOrder.setStoreId(clientInfo.getStoreId());

        //设置order的其他信息
//        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        consumerOrder.setOrderType(Constants.OrderType.ARK.getValue());
        consumerOrder.setPayState(Constants.PayState.NOT_PAY.getValue());
        //设置订单状态为“预约”
        consumerOrder.setState(Constants.OrderState.RESERVATION.getValue());

        //计算项目金额
        double totalPrice = consumerProjectInfoService.sumAllProjectPrice(consumerProjectInfos);
        consumerOrder.setTotalPrice(totalPrice);
        consumerOrder.setActualPrice(totalPrice);

        ConsumerOrder consumerOrderRes;
        try {
            consumerOrderRes = this.saveOrUpdate(consumerOrder);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return ResultJsonObject.getErrorResult(null, "智能柜开单失败！" + e.getMessage());
        }
        if (null == consumerOrderRes) {
            return ResultJsonObject.getErrorResult(null, "智能柜开单失败！保存订单信息失败。如有疑问，请联系管理员！");
        }

        String orderId = consumerOrder.getId();

        //保存订单项目信息
        if (null != consumerProjectInfos && !consumerProjectInfos.isEmpty()) {
            for (ConsumerProjectInfo consumerProjectInfo : consumerProjectInfos) {
                consumerProjectInfo.setConsumerOrderId(orderId);
                consumerProjectInfoService.saveOrUpdate(consumerProjectInfo);
            }
        }

        // 有效柜子分配逻辑
        Door emptyDoor = doorService.getUsefulDoor(arkSn);
        // door表数据更新，根据智能柜编号获取door对象，并更新状态为"预约状态"
        this.changeDoorState(emptyDoor, orderId, Constants.DoorState.USER_RESERVATION.getValue());
        // 数据保存完毕之后操作硬件，成功后返回成功，否则抛出异常进行回滚操作
        doorService.openDoorByDoorObject(emptyDoor);


        //推送微信消息给技师 需要给这个柜子相关的技师都推送
        staffService.sendWeChatMessageToStaff(consumerOrderRes, emptyDoor);

        return ResultJsonObject.getDefaultResult(consumerOrderRes.getId(), "订单生成成功！");
    }

    /**
     * 设置订单为取消状态
     *
     * @param orderId
     * @return
     */
    public ResultJsonObject cancelOrder(String orderId) throws ArgumentMissingException, OpenArkDoorFailedException, OpenArkDoorTimeOutException {
        ConsumerOrder consumerOrder = consumerOrderRepository.findById(orderId).orElse(null);
        if (null == consumerOrder) {
            return ResultJsonObject.getErrorResult(null, "未找到id为：" + orderId + " 的订单");
        }
        consumerOrder.setState(Constants.OrderState.CANCEL.getValue());
        this.updateOrder(consumerOrder);

        //获取订单对应的柜子信息
        Door door = doorRepository.findTopByOrderId(orderId);
        //更新door表数据
        this.changeDoorState(door, null, Constants.DoorState.EMPTY.getValue());
        //打开柜门
        doorService.openDoorByDoorObject(door);

        //TODO 用户取消服务订单的时候推送消息给技师

        return ResultJsonObject.getDefaultResult(orderId);
    }


    /**
     * 根据车牌号和门店ID查询客户开单时所需的信息
     *
     * @param licensePlate
     * @param storeId
     * @return
     */
    public ResultJsonObject loadClientInfoByLicensePlate(String licensePlate, String storeId) {
        //查找车辆
        Car car = carService.findCarByLicensePlateAndStoreId(licensePlate, storeId);
        if (null == car) {
            return ResultJsonObject.getErrorResult(null, "未找到对应车辆信息");
        }
        String clientId = car.getClientId();
        if (StringUtils.isEmpty(clientId)) {
            return ResultJsonObject.getErrorResult(null, "未找到该车牌绑定的客户信息");
        }
        Client client = (Client) clientService.getDetail(clientId).getData();
        if (null == client) {
            return ResultJsonObject.getErrorResult(null, "未找到该车牌绑定的客户信息");
        }
        if (client.getDelStatus()) {
            return ResultJsonObject.getErrorResult(null, "未找到该车牌绑定的客户信息");
        }

        double carBalance;
        Float balance = cardRepository.sumBalanceByClientId(clientId);
        if (null != balance) {
            //格式化精度
            carBalance = RoundTool.round(balance.doubleValue(), 2, BigDecimal.ROUND_HALF_UP);
        } else {
            carBalance = 0.0;
        }

        double consumeAmount = client.getConsumeAmount() == null ? 0.0 : client.getConsumeAmount();

        OrderClientInfo orderClientInfo = new OrderClientInfo();

        orderClientInfo.setCarId(car.getId());
        orderClientInfo.setLicensePlate(car.getLicensePlate());
        orderClientInfo.setCarBrand(car.getCarBrand());
        orderClientInfo.setCarType(car.getCarType());
        orderClientInfo.setStoreId(car.getStoreId());
        orderClientInfo.setLastMiles(String.valueOf(car.getLastMiles()));

        orderClientInfo.setClientId(client.getId());
        orderClientInfo.setClientName(client.getName());
        orderClientInfo.setPhone(client.getPhone());
        orderClientInfo.setIsMember(client.getMember());
        orderClientInfo.setHistoryConsumption(RoundTool.round(consumeAmount, 2, BigDecimal.ROUND_HALF_UP));

        orderClientInfo.setBalance(carBalance);


        return ResultJsonObject.getDefaultResult(orderClientInfo);
    }

    /**
     * 用户开柜取车
     *
     * @param orderId
     * @return
     */
    public ResultJsonObject orderFinish(String orderId) throws Exception {
        if (StringUtils.isEmpty(orderId)) {
            return ResultJsonObject.getCustomResult(orderId, ResultCode.PARAM_NOT_COMPLETE);
        }
        ConsumerOrder consumerOrder = consumerOrderRepository.findById(orderId).orElse(null);
        if (null == consumerOrder) {
            return ResultJsonObject.getCustomResult(orderId, ResultCode.RESULT_DATA_NONE);
        }
        //设置单据状态为"已取车（交车）"
        consumerOrder.setState(Constants.OrderState.HAND_OVER.getValue());
        consumerOrder.setDeliverTime(new Timestamp(System.currentTimeMillis()));

        ConsumerOrder res = this.updateOrder(consumerOrder);
        if (null == res) {
            return ResultJsonObject.getErrorResult(orderId, "单据状态更新失败，执行数据回滚");
        }


        //获取订单对应的柜子信息
        Door door = doorRepository.findTopByOrderId(orderId);
        //更新door表数据
        this.changeDoorState(door, null, Constants.DoorState.EMPTY.getValue());
        //打开柜门
        doorService.openDoorByDoorObject(door);


        //推送微信公众号消息，通知用户服务完全结束
        sendWeChatMsg(res);

        return ResultJsonObject.getDefaultResult(orderId);
    }

    /**
     * 技师去取车，提醒用户订单已经被受理
     *
     * @param orderId
     * @param staffId
     * @return
     */
    public ResultJsonObject pickCar(String orderId, String staffId) throws Exception {
        if (StringUtils.isEmpty(orderId)) {
            return ResultJsonObject.getCustomResult("The param 'orderId' is null", ResultCode.PARAM_NOT_COMPLETE);
        }
        if (StringUtils.isEmpty(staffId)) {
            return ResultJsonObject.getCustomResult("The param 'staffId' is null", ResultCode.PARAM_NOT_COMPLETE);
        }
        ConsumerOrder consumerOrder = consumerOrderRepository.findById(orderId).orElse(null);
        if (null == consumerOrder) {
            return ResultJsonObject.getCustomResult("Not found consumerOrder object by orderId : " + orderId, ResultCode.RESULT_DATA_NONE);
        }

        Staff staff = staffService.findById(staffId);
        if (null == staff) {
            return ResultJsonObject.getCustomResult("Not found staff object by staffId : " + staffId, ResultCode.RESULT_DATA_NONE);
        }

        consumerOrder.setPickTime(new Timestamp(System.currentTimeMillis()));
        consumerOrder.setState(Constants.OrderState.RECEIVE_CAR.getValue());
        consumerOrder.setPickCarStaffId(staffId);
        consumerOrder.setPickCarStaffName(staff.getName());
        ConsumerOrder orderRes = this.updateOrder(consumerOrder);
        if (null == orderRes) {
            return ResultJsonObject.getErrorResult(null, "单据状态更新失败");
        }


        //更新door表数据状态
        Door door = doorRepository.findTopByOrderId(orderId);
        this.changeDoorState(door, null, Constants.DoorState.EMPTY.getValue());
        // 调用硬件接口方法打开柜门
        doorService.openDoorByDoorObject(door);

        //推送微信公众号消息，通知用户已开始受理服务
        sendWeChatMsg(orderRes);

        return ResultJsonObject.getDefaultResult(orderId);
    }

    /**
     * 技师还车，提醒用户来取车
     *
     * @param orderObject
     * @return
     */
    public ResultJsonObject finishCar(OrderObject orderObject) throws Exception {
        ConsumerOrder consumerOrder = orderObject.getConsumerOrder();
        String arkSn = orderObject.getArkSn();

        if (StringUtils.isEmpty(arkSn)) {
            logger.error("智能柜-车辆完工 失败：参数中的arkSn对象为空，无法分配智能柜");
            return ResultJsonObject.getErrorResult(null, "智能柜-车辆完工 失败：参数中的arkSn对象为空，无法分配智能柜");
        }

        if (null == consumerOrder) {
            logger.error("智能柜-车辆完工 失败：参数中的consumerOrder对象为空");
            return ResultJsonObject.getErrorResult(null, "智能柜-车辆完工 失败：参数中的consumerOrder对象为空");
        }

        String orderId = consumerOrder.getId();
        if (StringUtils.isEmpty(orderId)) {
            return ResultJsonObject.getCustomResult("The param 'orderId' is null", ResultCode.PARAM_NOT_COMPLETE);
        }


        consumerOrder.setFinishTime(new Timestamp(System.currentTimeMillis()));
        consumerOrder.setState(Constants.OrderState.SERVICE_FINISH.getValue());

        ConsumerOrder order = this.updateOrder(consumerOrder);
        if (null == order) {
            return ResultJsonObject.getErrorResult(null, "单据状态更新失败");
        }

        // 有效柜子分配逻辑
        Door emptyDoor = doorService.getUsefulDoor(arkSn);
        // 更新door表数据状态
        this.changeDoorState(emptyDoor, orderId, Constants.DoorState.STAFF_FINISH.getValue());
        // 调用硬件接口方法打开柜门
        doorService.openDoorByDoorObject(emptyDoor);


        // 推送微信公众号消息，通知用户取车
        sendWeChatMsg(order);

        return ResultJsonObject.getDefaultResult(orderId);
    }

    /**
     * 微信支付成功后回调，处理订单业务
     *
     * @param orderId
     * @return
     */
    public ResultJsonObject wechatPaySuccess(String orderId) {
        if (StringUtils.hasText(orderId)) {
            //判断是哪种订单
            String firstCharacter = orderId.substring(0, 1);
            switch (firstCharacter) {
                case "A":
                    return this.arkPaySuccess(orderId);
                case "C":
                    try {
                        return this.buyCardPaySuccess(orderId);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                        e.printStackTrace();
                        return ResultJsonObject.getErrorResult(null);
                    }
                default:
                    return ResultJsonObject.getErrorResult(null, "该类型的订单不支持微信支付，请联系平台管理员核实。");
            }
        }
        String msg = "orderId为空，无法执行回调业务，会出现重复支付的情况，请联系门店或管理平台处理。";
        logger.error(msg);
        return ResultJsonObject.getErrorResult(null, msg);
    }


    /**
     * 智能柜服订单支付成功后更新支付状态
     *
     * @param orderId
     * @return
     */
    public ResultJsonObject arkPaySuccess(String orderId) {
        ConsumerOrder consumerOrder = consumerOrderRepository.findById(orderId).orElse(null);
        if (null == consumerOrder) {
            return ResultJsonObject.getCustomResult(null, ResultCode.RESULT_DATA_NONE);
        }
        consumerOrder.setPayState(Constants.PayState.FINISH_PAY.getValue());
        ConsumerOrder orderRes = updateOrder(consumerOrder);
        if (null == orderRes) {
            ResultJsonObject.getErrorResult(null, "单据数据更新失败");
        }
        return ResultJsonObject.getDefaultResult(orderId);
    }

    /**
     * 购买卡券的订单支付成功后更新支付状态，并更新对应的卡券信息
     *
     * @param orderId
     * @return
     */
    public ResultJsonObject buyCardPaySuccess(String orderId) throws Exception {
        ConsumerOrder consumerOrder = consumerOrderRepository.findById(orderId).orElse(null);
        if (null == consumerOrder) {
            throw new ObjectNotFoundException("订单数据未查询到，无法更新卡券状态和订单状态");
        }
        consumerOrder.setPayState(Constants.PayState.FINISH_PAY.getValue());

        //更新卡/券的状态，是其可以使用
        String cardOrCouponId = consumerOrder.getCardOrCouponId();
        if (StringUtils.isEmpty(cardOrCouponId)) {
            throw new ArgumentMissingException("订单中的卡券ID为空，无法更新订单状态");
        }
        Card card = cardRepository.findById(cardOrCouponId).orElse(null);
        Coupon coupon = couponRepository.findById(cardOrCouponId).orElse(null);
        if (null == card && null == coupon) {
            throw new ObjectNotFoundException("未找到卡/券的对象，无法更新卡券状态和订单状态");
        }

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        if (null != card) {
            card.setDelStatus(Constants.DelStatus.NORMAL.isValue());
            card.setPayDate(currentTime);
            card.setPayMethod(Constants.PayMethod.WECHAT_PAY.getCode());
            com.freelycar.saas.project.entity.CardService cardServiceObject = cardServiceRepository.findById(card.getCardServiceId()).orElse(null);
            if (cardServiceObject == null) {
                throw new ObjectNotFoundException("对象cardServiceObject为空值，无法更新卡券状态和订单状态");
            }
            card.setExpirationDate(TimestampUtil.getExpirationDateForYear(cardServiceObject.getValidTime()));
            card.setPayDate(currentTime);
            cardRepository.save(card);
        }
        if (null != coupon) {
            coupon.setDelStatus(Constants.DelStatus.NORMAL.isValue());
            coupon.setPayMethod(Constants.PayMethod.WECHAT_PAY.getCode());
            com.freelycar.saas.project.entity.CouponService couponServiceObject = couponServiceRepository.findById(coupon.getCouponServiceId()).orElse(null);
            if (couponServiceObject == null) {
                throw new ObjectNotFoundException("对象couponServiceObject为空值，无法更新卡券状态和订单状态");
            }
            coupon.setDeadline(TimestampUtil.getExpirationDateForMonth(couponServiceObject.getValidTime()));
            couponRepository.save(coupon);
        }

        //更新订单状态
        ConsumerOrder orderRes = updateOrder(consumerOrder);
        if (null == orderRes) {
            throw new UpdateDataErrorException("单据数据更新失败，无法更新卡券状态和订单状态");
        }


        return ResultJsonObject.getDefaultResult(orderId);
    }

    public void sendWeChatMsg(ConsumerOrder consumerOrder) {
        //推送微信公众号消息，通知用户已开始受理服务
        String phone = consumerOrder.getPhone();
        String openId = wxUserInfoService.getOpenId(phone);
        if (StringUtils.isEmpty(openId)) {
            logger.error("未获得到对应的openId，微信消息推送失败");
        } else {
            WechatTemplateMessage.orderChanged(consumerOrder, openId);
        }
    }

    /**
     * 改变door表的数据状态
     *
     * @param arkSn
     * @param doorSn
     * @param orderId
     * @param doorState
     * @throws Exception
     */
    private void changeDoorState(String arkSn, String doorSn, String orderId, int doorState) throws Exception {
        Door door = doorRepository.findTopByArkSnAndDoorSn(arkSn, doorSn);
        this.changeDoorState(door, orderId, doorState);
    }

    /**
     * 改变door表的数据状态
     *
     * @param door
     * @param orderId
     * @param doorState
     * @throws Exception
     */
    private void changeDoorState(Door door, String orderId, int doorState) throws ArgumentMissingException {
        if (null == door) {
            throw new ArgumentMissingException("没找到分配的智能柜door表信息，无法更新状态。预约服务状态终止。");
        }
        door.setOrderId(orderId);
        door.setState(doorState);
        doorRepository.save(door);
    }

    /**
     * 生成购买卡券的订单
     *
     * @param client
     * @param price
     * @param cardOrCouponId
     * @return
     * @throws Exception
     */
    public ConsumerOrder generateOrderForBuyCardOrCoupon(Client client, double price, String cardOrCouponId) throws Exception {
        if (null == client || StringUtils.isEmpty(cardOrCouponId)) {
            throw new ArgumentMissingException("生成购买卡券的订单失败，原因：参数缺失");
        }

        String clientName = client.getTrueName();
        if (StringUtils.isEmpty(clientName)) {
            clientName = client.getName();
        }

        ConsumerOrder order = new ConsumerOrder();
        order.setPayState(Constants.PayState.NOT_PAY.getValue());
        order.setOrderType(Constants.OrderType.CARD.getValue());
        order.setClientId(client.getId());
        order.setTotalPrice(price);
        order.setActualPrice(price);

        order.setClientName(clientName);
        order.setPhone(client.getPhone());
        order.setIsMember(client.getMember());
        order.setGender(client.getGender());
        order.setStoreId(client.getStoreId());
        order.setCardOrCouponId(cardOrCouponId);

        return this.saveOrUpdate(order);
    }


    /**
     * 查询消费记录（分页）
     *
     * @param params
     * @param currentPage
     * @param pageSize
     * @return
     * @throws ArgumentMissingException
     */
    public PaginationRJO orderRecord(Map<String, Object> params, Integer currentPage, Integer pageSize) throws ArgumentMissingException {
        if (null == params) {
            throw new ArgumentMissingException("参数params为空值，无法查询消费记录");
        }
        String clientId = (String) params.get("clientId");
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");

        if (StringUtils.isEmpty(clientId)) {
            throw new ArgumentMissingException("参数clientId为空值，无法查询消费记录");
        }

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT co.id, ( SELECT group_concat( cpi.projectName ) FROM consumerprojectinfo cpi WHERE cpi.consumerOrderId = co.id ) AS projectNames, co.actualPrice AS cost, CONCAT((CASE co.firstPayMethod WHEN 0 THEN '储值卡' WHEN 1 THEN '现金' WHEN 2 THEN '微信' WHEN 3 THEN '支付宝' WHEN 4 THEN '易付宝' WHEN 5 THEN '刷卡' ELSE '无' END ), (CASE co.secondPayMethod WHEN 0 THEN '，储值卡' WHEN 1 THEN '，现金' WHEN 2 THEN '，微信' WHEN 3 THEN '，支付宝' WHEN 4 THEN '，易付宝' WHEN 5 THEN '，刷卡' ELSE '' END ) ) AS payMethod, (CASE (( CASE co.firstPayMethod WHEN 0 THEN TRUE ELSE FALSE END ) OR ( CASE co.secondPayMethod WHEN 0 THEN TRUE ELSE FALSE END )) WHEN TRUE THEN '是' ELSE '否' END) AS useCard, co.createTime AS serviceTime FROM consumerorder co WHERE co.clientId = '").append(clientId).append("' AND co.delStatus = 0 AND payState = 2 ");
        if (StringUtils.hasText(startTime)) {
            sql.append(" AND co.createTime > '").append(startTime).append(" 00:00:00' ");
        }
        if (StringUtils.hasText(endTime)) {
            sql.append(" AND co.createTime <= '").append(endTime).append(" 23:59:59' ");
        }
        sql.append(" ORDER BY co.createTime DESC ");

        Pageable pageable = PageableTools.basicPage(currentPage, pageSize);

        EntityManager em = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
        Query nativeQuery = em.createNativeQuery(sql.toString());
        nativeQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(OrderRecordObject.class));

        int total = nativeQuery.getResultList().size();
        @SuppressWarnings({"unused", "unchecked"})
        List<OrderRecordObject> orderRecordObjects = nativeQuery.setFirstResult(MySQLPageTool.getStartPosition(currentPage, pageSize)).setMaxResults(pageSize).getResultList();

        //关闭em
        em.close();

        @SuppressWarnings("unchecked")
        Page<OrderRecordObject> page = new PageImpl(orderRecordObjects, pageable, total);

        return PaginationRJO.of(page);
    }


    public ResultJsonObject listOrderParticulars(String storeId, String startTime, String endTime, Integer currentPage, Integer pageSize, boolean export) throws ArgumentMissingException {

        if (StringUtils.isEmpty(storeId)) {
            throw new ArgumentMissingException("参数storeId为空值，无法查询流水明细");
        }

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT co.id, co.carBrand, co.licensePlate, co.clientName, co.phone, ( SELECT group_concat( cpi.projectName ) FROM consumerprojectinfo cpi WHERE cpi.consumerOrderId = co.id ) AS projectNames, co.actualPrice AS cost, co.createTime AS serviceTime, (case co.isMember when 1 then '是' else '否' end) as isMember FROM consumerorder co WHERE co.storeId = '").append(storeId).append("' AND co.delStatus = 0 AND payState = 2 ");
        if (StringUtils.hasText(startTime)) {
            sql.append(" AND co.createTime > '").append(startTime).append(" 00:00:00' ");
        }
        if (StringUtils.hasText(endTime)) {
            sql.append(" AND co.createTime <= '").append(endTime).append(" 23:59:59' ");
        }
        sql.append(" ORDER BY co.createTime DESC ");


        EntityManager em = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
        Query nativeQuery = em.createNativeQuery(sql.toString());
        nativeQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(OrderParticulars.class));

        if (export) {
            @SuppressWarnings({"unused", "unchecked"})
            List<OrderParticulars> orderParticulars = nativeQuery.getResultList();

            //关闭em
            em.close();

            return ResultJsonObject.getDefaultResult(orderParticulars);
        } else {
            Pageable pageable = PageableTools.basicPage(currentPage, pageSize);
            int total = nativeQuery.getResultList().size();
            @SuppressWarnings({"unused", "unchecked"})
            List<OrderParticulars> orderParticulars = nativeQuery.setFirstResult(MySQLPageTool.getStartPosition(currentPage, pageSize)).setMaxResults(pageSize).getResultList();

            //关闭em
            em.close();

            @SuppressWarnings("unchecked")
            Page<OrderRecordObject> page = new PageImpl(orderParticulars, pageable, total);

            return ResultJsonObject.getDefaultResult(PaginationRJO.of(page));
        }
    }
}
