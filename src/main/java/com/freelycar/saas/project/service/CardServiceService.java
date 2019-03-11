package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.*;
import com.freelycar.saas.exception.ArgumentMissingException;
import com.freelycar.saas.exception.ObjectNotFoundException;
import com.freelycar.saas.project.entity.Card;
import com.freelycar.saas.project.entity.CardService;
import com.freelycar.saas.project.entity.Client;
import com.freelycar.saas.project.entity.ConsumerOrder;
import com.freelycar.saas.project.repository.CardRepository;
import com.freelycar.saas.project.repository.CardServiceRepository;
import com.freelycar.saas.util.RoundTool;
import com.freelycar.saas.util.UpdateTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static com.freelycar.saas.basic.wrapper.ResultCode.RESULT_DATA_NONE;

@Service
@Transactional(rollbackFor = Exception.class)
public class CardServiceService {
    private Logger logger = LoggerFactory.getLogger(CardServiceService.class);

    @Autowired
    private CardServiceRepository cardServiceRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ConsumerOrderService consumerOrderService;

    @Autowired
    private CardRepository cardRepository;

    /**
     * 新增/修改卡类对象
     *
     * @param cardService
     * @return
     */
    public ResultJsonObject modify(CardService cardService) {
        try {
            //验重
            if (this.checkRepeatName(cardService)) {
                return ResultJsonObject.getErrorResult(null, "已包含名称为：“" + cardService.getName() + "”的数据，不能重复添加。");
            }

            //是否有ID，判断时新增还是修改
            String id = cardService.getId();
            if (StringUtils.isEmpty(id)) {
                cardService.setDelStatus(Constants.DelStatus.NORMAL.isValue());
                cardService.setCreateTime(new Timestamp(System.currentTimeMillis()));
                cardService.setBookOnline(false);
            } else {
                Optional<CardService> optional = cardServiceRepository.findById(id);
                //判断数据库中是否有该对象
                if (!optional.isPresent()) {
                    logger.error("修改失败，原因：" + CardService.class + "中不存在id为 " + id + " 的对象");
                    return ResultJsonObject.getErrorResult(null);
                }
                CardService source = optional.get();
                //将目标对象（projectType）中的null值，用源对象中的值替换
                UpdateTool.copyNullProperties(source, cardService);
            }
            //执行保存/修改
            return ResultJsonObject.getDefaultResult(cardServiceRepository.saveAndFlush(cardService));
        } catch (Exception e) {
            return ResultJsonObject.getErrorResult(null);
        }
    }

    /**
     * 验证项目是否重复
     * true：重复；false：不重复
     *
     * @param cardService
     * @return
     */
    private boolean checkRepeatName(CardService cardService) {
        List<CardService> cardServiceList;
        if (null != cardService.getId()) {
            cardServiceList = cardServiceRepository.checkRepeatName(cardService.getId(), cardService.getName(), cardService.getStoreId());
        } else {
            cardServiceList = cardServiceRepository.checkRepeatName(cardService.getName(), cardService.getStoreId());
        }
        return cardServiceList.size() != 0;
    }

    /**
     * 获取卡类详情
     *
     * @param id
     * @return
     */
    public ResultJsonObject getDetail(String id) {
        return ResultJsonObject.getDefaultResult(cardServiceRepository.findById(id));
    }


    /**
     * 查询卡类列表
     *
     * @param storeId
     * @param currentPage
     * @param pageSize
     * @return
     */
    public PaginationRJO list(String storeId, Integer currentPage, Integer pageSize, String name) {
        logger.debug("storeId:" + storeId);
        Page<CardService> cardServicePage = cardServiceRepository.findAllByDelStatusAndStoreIdAndNameContaining(Constants.DelStatus.NORMAL.isValue(), storeId, name, PageableTools.basicPage(currentPage, pageSize));
        return PaginationRJO.of(cardServicePage);
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
            int result = cardServiceRepository.delById(id);
            if (result != 1) {
                return ResultJsonObject.getErrorResult(id, "删除失败," + RESULT_DATA_NONE);
            }
        } catch (Exception e) {
            return ResultJsonObject.getErrorResult(id, "删除失败，删除操作出现异常");
        }
        return ResultJsonObject.getDefaultResult(id, "删除成功");
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
            int result = cardServiceRepository.uppById(id);
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
            int result = cardServiceRepository.lowById(id);
            if (result != 1) {
                return ResultJsonObject.getErrorResult(id, "下架失败," + RESULT_DATA_NONE);
            }
        } catch (Exception e) {
            return ResultJsonObject.getErrorResult(id, "下架失败，下架操作出现异常");
        }
        return ResultJsonObject.getDefaultResult(id, "下架成功");
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
            cardServiceRepository.delById(id);
        }
        return ResultJsonObject.getDefaultResult(null);
    }


    /**
     * 查找上架在售的会员卡
     *
     * @param storeId
     * @return
     */
    public List<CardService> findOnSaleCards(String storeId) {
        return cardServiceRepository.findByStoreIdAndDelStatusAndBookOnline(storeId, Constants.DelStatus.NORMAL.isValue(), true);
    }

    /**
     * 生成微信端订单
     *
     * @param clientId
     * @param cardServiceId
     * @return
     * @throws Exception
     */
    public String generateCardOrder(String clientId, String cardServiceId) throws Exception {
        if (StringUtils.isEmpty(clientId)) {
            throw new ArgumentMissingException("参数clientId为空，生成会员卡购买订单失败！");
        }
        if (StringUtils.isEmpty(cardServiceId)) {
            throw new ArgumentMissingException("参数cardServiceId为空，生成会员卡购买订单失败！");
        }

        Client client = clientService.findById(clientId);
        if (null == client) {
            throw new ObjectNotFoundException("用户信息查找失败，无法生成购买订单，请稍后再试或联系客服。");
        }

        CardService cardServiceObject = cardServiceRepository.findById(cardServiceId).orElse(null);
        if (null == cardServiceObject) {
            throw new ObjectNotFoundException("会员卡信息查找失败，无法生成购买订单，请稍后再试或联系客服。");
        }

        String clientStoreId = client.getStoreId();
        String cardServiceStoreId = cardServiceObject.getStoreId();
        if (null != clientStoreId && null != cardServiceStoreId && !clientStoreId.equals(cardServiceStoreId)) {
            logger.error("clientStoreId:" + clientStoreId);
            logger.error("cardServiceStoreId:" + cardServiceStoreId);
            throw new Exception("用户信息与会员卡信息所属门店不同，无法生成购买订单，请核实或联系客服。");
        }

        double price = RoundTool.round(cardServiceObject.getPrice().doubleValue(), 2, BigDecimal.ROUND_HALF_UP);

        //生成card对象（未支付前是不可用的：delStatus是1）
        Card card = new Card();
        card.setDelStatus(Constants.DelStatus.DELETE.isValue());
        //为字段赋默认值
        card.setCreateTime(new Timestamp(System.currentTimeMillis()));
        card.setClientId(clientId);
        card.setBalance(cardServiceObject.getActualPrice());
        card.setActualPrice(cardServiceObject.getActualPrice());
        card.setFailed(false);
        card.setName(cardServiceObject.getName());
        card.setPrice(cardServiceObject.getPrice());
        card.setStoreId(cardServiceStoreId);
        card.setCardServiceId(cardServiceId);
        card.setContent(cardServiceObject.getComment());

        Card cardRes = cardRepository.saveAndFlush(card);
        if (null == cardRes) {
            throw new Exception("会员卡数据生成异常，无法生成购买订单，请核实或联系客服。");
        }

        //生成订单
        ConsumerOrder consumerOrder = consumerOrderService.generateOrderForBuyCardOrCoupon(client, price, cardRes.getId());

        String orderId = consumerOrder.getId();

        //更新卡号为订单id
        cardRes.setCardNumber(orderId);
        cardRepository.save(cardRes);

        return orderId;
    }
}
