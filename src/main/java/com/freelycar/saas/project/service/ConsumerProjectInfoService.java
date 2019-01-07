package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.Constants;
import com.freelycar.saas.project.entity.ConsumerProjectInfo;
import com.freelycar.saas.project.repository.ConsumerProjectInfoRepository;
import com.freelycar.saas.util.UpdateTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * @author tangwei - Toby
 * @date 2018-12-28
 * @email toby911115@gmail.com
 */
@Service
@Transactional
public class ConsumerProjectInfoService {
    @Autowired
    private ConsumerProjectInfoRepository consumerProjectInfoRepository;

    /**
     * 保存消费订单服务项目对象
     *
     * @param consumerProjectInfo
     * @return
     */
    public ConsumerProjectInfo saveOrUpdate(ConsumerProjectInfo consumerProjectInfo) {
        if (null == consumerProjectInfo) {
            return null;
        }
        String id = consumerProjectInfo.getId();

        if (StringUtils.isEmpty(id)) {
            consumerProjectInfo.setDelStatus(Constants.DelStatus.NORMAL.isValue());
            consumerProjectInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
        } else {
            Optional<ConsumerProjectInfo> optional = consumerProjectInfoRepository.findById(id);
            if (!optional.isPresent()) {
                return null;
            }
            ConsumerProjectInfo source = optional.get();
            UpdateTool.copyNullProperties(source, consumerProjectInfo);
        }
        return consumerProjectInfoRepository.save(consumerProjectInfo);
    }
}
