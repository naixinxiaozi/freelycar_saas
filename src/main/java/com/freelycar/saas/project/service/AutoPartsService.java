package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.Constants;
import com.freelycar.saas.project.entity.AutoParts;
import com.freelycar.saas.project.repository.AutoPartsRepository;
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
 * @date 2018-12-28
 * @email toby911115@gmail.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AutoPartsService {
    @Autowired
    private AutoPartsRepository autoPartsRepository;

    /**
     * 保存配件信息
     *
     * @param autoParts
     * @return
     */
    public AutoParts saveOrUpdate(AutoParts autoParts) {
        if (null == autoParts) {
            return null;
        }

        String id = autoParts.getId();
        if (StringUtils.isEmpty(id)) {
            autoParts.setCreateTime(new Timestamp(System.currentTimeMillis()));
            autoParts.setDelStatus(Constants.DelStatus.NORMAL.isValue());
        } else {
            Optional<AutoParts> optional = autoPartsRepository.findById(id);
            if (!optional.isPresent()) {
                return null;
            }
            AutoParts source = optional.get();
            UpdateTool.copyNullProperties(source, autoParts);
        }
        return autoPartsRepository.save(autoParts);
    }

    public List<AutoParts> getAllAutoPartsByOrderId(String orderId) {
        return autoPartsRepository.findAllByDelStatusAndConsumerOrderIdOrderByCreateTimeAsc(Constants.DelStatus.NORMAL.isValue(), orderId);
    }
}
