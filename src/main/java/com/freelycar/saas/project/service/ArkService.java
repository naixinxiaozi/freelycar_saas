package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.Constants;
import com.freelycar.saas.basic.wrapper.ResultCode;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.Ark;
import com.freelycar.saas.project.repository.ArkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author tangwei - Toby
 * @date 2019-02-18
 * @email toby911115@gmail.com
 */
@Service
public class ArkService {
    @Autowired
    private ArkRepository arkRepository;


    public ResultJsonObject getCurrentArkLocation(String arkSn) {
        if (StringUtils.hasText(arkSn)) {
            Ark ark = arkRepository.findTopBySnAndDelStatus(arkSn, Constants.DelStatus.NORMAL.isValue());
            if (null == ark) {
                return ResultJsonObject.getCustomResult(arkSn, ResultCode.RESULT_DATA_NONE);
            }
            return ResultJsonObject.getDefaultResult(ark.getLocation());
        }
        return ResultJsonObject.getCustomResult(arkSn, ResultCode.PARAM_NOT_COMPLETE);
    }
}
