package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.Constants;
import com.freelycar.saas.basic.wrapper.PageableTools;
import com.freelycar.saas.basic.wrapper.PaginationRJO;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.*;
import com.freelycar.saas.project.model.OrderClientInfo;
import com.freelycar.saas.project.model.OrderListParam;
import com.freelycar.saas.project.model.OrderObject;
import com.freelycar.saas.project.model.PayOrder;
import com.freelycar.saas.project.repository.CardRepository;
import com.freelycar.saas.project.repository.ConsumerOrderRepository;
import com.freelycar.saas.util.OrderIDGenerator;
import com.freelycar.saas.util.RoundTool;
import com.freelycar.saas.util.UpdateTool;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author tangwei - Toby
 * @date 2018-12-28
 * @email toby911115@gmail.com
 */
@Service
@Transactional
public class ConsumerOrderService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ConsumerOrderRepository consumerOrderRepository;

    @Autowired
    private ConsumerProjectInfoService consumerProjectInfoService;

    @Autowired
    private AutoPartsService autoPartsService;

    @Autowired
    private CouponService couponService;

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

    /**
     * 查询可接车的智能柜预约
     *
     * @param licensePlate
     * @return
     */
    public List<ConsumerOrder> listReservationOrders(String licensePlate, String storeId) {
        licensePlate = StringUtils.isEmpty(licensePlate) ? "" : licensePlate;
        return consumerOrderRepository.findAllByStoreIdAndOrderTypeAndStateAndDelStatusAndLicensePlateContainingOrderByCreateTimeAsc(storeId, Constants.OrderType.ARK.getValue(), Constants.OrderState.RESERVATION.getValue(), Constants.DelStatus.NORMAL.isValue(), licensePlate);
    }


    /**
     * 查询可完工的智能柜订单
     *
     * @param licensePlate
     * @param storeId
     * @return
     */
    public List<ConsumerOrder> listServicingOrders(String licensePlate, String storeId) {
        licensePlate = StringUtils.isEmpty(licensePlate) ? "" : licensePlate;
        return consumerOrderRepository.findAllByStoreIdAndOrderTypeAndStateAndDelStatusAndLicensePlateContainingOrderByPickTimeAsc(storeId, Constants.OrderType.ARK.getValue(), Constants.OrderState.RECEIVE_CAR.getValue(), Constants.DelStatus.NORMAL.isValue(), licensePlate);
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

        List<ConsumerProjectInfo> consumerProjectInfos = consumerProjectInfoService.getAllProjectInfoByOrderId(id);

        List<AutoParts> autoPartsList = autoPartsService.getAllAutoPartsByOrderId(id);

        orderObject.setConsumerOrder(optionalConsumerOrder.get());
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
     * 单据列表条件查询
     *
     * @param params
     * @return
     */
    public ResultJsonObject list(String storeId, Integer currentPage, Integer pageSize, OrderListParam params) {
        String orderId = params.getOrderId();
        String licensePlate = params.getLicensePlate();

        //TODO 如何查出订单类型，还得考虑一下
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
        ConsumerOrder res = consumerOrderRepository.findTopByClientIdAndOrderTypeAndDelStatusAndStateLessThan(clientId, Constants.OrderType.ARK.getValue(), Constants.DelStatus.NORMAL.isValue(), Constants.OrderState.HAND_OVER.getValue());
        logger.info("res:", res);

        return ResultJsonObject.getDefaultResult(res);
    }

    /**
     * 智能柜开单
     *
     * @param orderObject
     * @return
     */
    public ResultJsonObject arkHandleOrder(OrderObject orderObject) {
        //获取提交过来的数据
        ConsumerOrder consumerOrder = orderObject.getConsumerOrder();
        List<ConsumerProjectInfo> consumerProjectInfos = orderObject.getConsumerProjectInfos();

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

        //TODO 数据保存完毕之后操作硬件，成功后返回成功，否则抛出异常进行回滚操作
        // door表数据更新


        return ResultJsonObject.getDefaultResult(consumerOrderRes.getId(), "订单生成成功！");
    }

    /**
     * 设置订单为取消状态
     *
     * @param orderId
     * @return
     */
    public ResultJsonObject cancelOrder(String orderId) {
        ConsumerOrder consumerOrder = consumerOrderRepository.findById(orderId).orElse(null);
        if (null == consumerOrder) {
            return ResultJsonObject.getErrorResult(null, "未找到id为：" + orderId + " 的订单");
        }
        consumerOrder.setState(Constants.OrderState.CANCEL.getValue());
        this.updateOrder(consumerOrder);
        //TODO 数据保存完毕之后操作硬件，成功后返回成功，否则抛出异常进行回滚操作
        // door表数据更新

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
            carBalance = RoundTool.round(balance.doubleValue(), 2, BigDecimal.ROUND_DOWN);
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
        orderClientInfo.setHistoryConsumption(RoundTool.round(consumeAmount, 2, BigDecimal.ROUND_DOWN));

        orderClientInfo.setBalance(carBalance);


        return ResultJsonObject.getDefaultResult(orderClientInfo);
    }

}
