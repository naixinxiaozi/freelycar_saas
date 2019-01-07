package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.WxUserInfo;
import com.freelycar.saas.project.repository.CarRepository;
import com.freelycar.saas.project.repository.WxUserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * @author tangwei
 * @date 2018/9/25
 */
@Service
@Transactional
public class WxUserInfoService {

    @Autowired
    private WxUserInfoRepository wxUserInfoRepository;

    @Autowired
    private CarRepository carRepository;

    /**
     * 查找微信用户对象
     *
     * @param id
     * @return
     */
    public WxUserInfo findById(String id) {
        Optional<WxUserInfo> optionalWxUserInfo = wxUserInfoRepository.findById(id);
        if (!optionalWxUserInfo.isPresent()) {
            return null;
        }
        return optionalWxUserInfo.get();
    }

    public ResultJsonObject getPersonalInfo(String id) {
        //查找微信用户对象
        WxUserInfo wxUserInfo = this.findById(id);
        if (null == wxUserInfo) {
            return ResultJsonObject.getErrorResult(id, "找不到id为：" + id + "的微信用户信息。");
        }

        //获取车辆信息
        String defaultStoreId = wxUserInfo.getDefaultStoreId();
        if (StringUtils.isEmpty(defaultStoreId)) {

        }
        return ResultJsonObject.getErrorResult(null);
    }
}
