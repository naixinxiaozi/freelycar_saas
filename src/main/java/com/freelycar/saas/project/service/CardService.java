package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.DelStatus;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.Card;
import com.freelycar.saas.project.repository.CardRepository;
import com.freelycar.saas.project.repository.CardServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2018-12-27
 * @email toby911115@gmail.com
 */
@Service
@Transactional
public class CardService {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardServiceRepository cardServiceRepository;


    public ResultJsonObject handleCard(Card card) {
        if (null == card) {
            return ResultJsonObject.getErrorResult(null, "开卡失败！参数card为Null！");
        }

        /* 验证非空 */
        String clientId = card.getClientId();
        String cardServiceId = card.getCardServiceId();
        String cardNumber = card.getCardNumber();

        if (StringUtils.isEmpty(clientId)) {
            return ResultJsonObject.getErrorResult(null, "开卡失败！clientId为Null！");
        }

        if (StringUtils.isEmpty(cardServiceId)) {
            return ResultJsonObject.getErrorResult(null, "开卡失败！cardServiceId为Null！");
        }

        if (StringUtils.isEmpty(cardNumber)) {
            return ResultJsonObject.getErrorResult(null, "开卡失败！cardNumber为Null！");
        }

        //验证卡号是否重复
        if (this.isCardNumberRepeat(card)) {
            return ResultJsonObject.getErrorResult(null, "开卡失败！卡号重复！如有疑问，请联系管理员！");
        }

        //查询会员卡销售信息
        com.freelycar.saas.project.entity.CardService cardServiceObject = cardServiceRepository.getOne(cardServiceId);
        if (null == cardServiceObject) {
            return ResultJsonObject.getErrorResult(null, "开卡失败！未查询到选择的卡类信息！如有疑问，请联系管理员！");
        }

        //为字段赋默认值
        card.setDelStatus(DelStatus.EFFECTIVE.isValue());
        card.setCreateTime(new Timestamp(System.currentTimeMillis()));
        card.setPayDate(new Timestamp(System.currentTimeMillis()));
        card.setBalance(cardServiceObject.getActualPrice());
        card.setActualPrice(cardServiceObject.getActualPrice());
        card.setFailed(false);
        card.setName(cardServiceObject.getName());
        card.setPrice(cardServiceObject.getPrice());
        card.setExpirationDate(this.getExpirationDate(cardServiceObject.getValidTime()));



        return ResultJsonObject.getDefaultResult(cardRepository.saveAndFlush(card));
    }

    private boolean isCardNumberRepeat(Card card) {
        List<Card> cardList = cardRepository.findByCardNumberAndDelStatusAndStoreId(card.getCardNumber(), DelStatus.EFFECTIVE.isValue(), card.getStoreId());
        return !cardList.isEmpty();
    }

    private Timestamp getExpirationDate(int validTime) {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int year = currentYear + validTime;
        calendar.set(Calendar.YEAR, year);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 获取会员卡详情
     *
     * @param id
     * @return
     */
    public ResultJsonObject getDetail(String id) {
        return ResultJsonObject.getDefaultResult(cardRepository.findById(id));
    }
}
