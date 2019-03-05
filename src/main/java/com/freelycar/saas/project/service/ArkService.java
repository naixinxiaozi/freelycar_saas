package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.Constants;
import com.freelycar.saas.basic.wrapper.ResultCode;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.exception.ArgumentMissingException;
import com.freelycar.saas.exception.DataIsExistException;
import com.freelycar.saas.exception.ObjectNotFoundException;
import com.freelycar.saas.project.entity.Ark;
import com.freelycar.saas.project.repository.ArkRepository;
import com.freelycar.saas.util.UpdateTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;

/**
 * @author tangwei - Toby
 * @date 2019-02-18
 * @email toby911115@gmail.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ArkService {
    @Autowired
    private ArkRepository arkRepository;

    @Autowired
    private DoorService doorService;


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

    /**
     * 修改智能柜信息
     *
     * @param ark
     * @return
     */
    public Ark modify(Ark ark) throws ArgumentMissingException, ObjectNotFoundException, DataIsExistException {
        if (null == ark) {
            throw new ArgumentMissingException("ark值为空");
        }
        String id = ark.getId();
        //新增
        if (StringUtils.isEmpty(id)) {
            return this.addArk(ark);
        } else {
            Ark source = arkRepository.findById(id).orElse(null);
            if (null == source) {
                throw new ObjectNotFoundException("未找到id为：" + id + " 的ark对象");
            }
            UpdateTool.copyNullProperties(source, ark);
        }
        return arkRepository.save(ark);
    }

    /**
     * 添加智能柜
     *
     * @param ark
     * @return
     * @throws ArgumentMissingException
     * @throws DataIsExistException
     */
    public Ark addArk(Ark ark) throws ArgumentMissingException, DataIsExistException {
        if (null == ark) {
            throw new ArgumentMissingException("ark对象为空");
        }
        //获取sn（智能柜IMEI)
        String sn = ark.getSn();
        if (StringUtils.isEmpty(sn)) {
            throw new ArgumentMissingException("ark对象中sn值为空");
        }
        //如果sn已经有了，则抛出异常
        Ark arkExist = arkRepository.findTopBySnAndDelStatus(sn, Constants.DelStatus.NORMAL.isValue());
        if (null != arkExist) {
            throw new DataIsExistException("sn为：" + sn + " 的ark对象已经存在，不可以重复添加");
        }

        //初始化数据
        ark.setDelStatus(Constants.DelStatus.NORMAL.isValue());
        ark.setCreateTime(new Timestamp(System.currentTimeMillis()));

        Ark result = arkRepository.save(ark);

        //添加子表数据
        doorService.generateDoors(result);

        return result;
    }

    /**
     * @param arkSn
     * @return
     */
    public ResultJsonObject getArkInfo(String arkSn) {
        if (StringUtils.hasText(arkSn)) {
            Ark ark = arkRepository.findTopBySnAndDelStatus(arkSn, Constants.DelStatus.NORMAL.isValue());
            if (null == ark) {
                return ResultJsonObject.getCustomResult(arkSn, ResultCode.RESULT_DATA_NONE);
            }
            return ResultJsonObject.getDefaultResult(ark);
        }
        return ResultJsonObject.getCustomResult(arkSn, ResultCode.PARAM_NOT_COMPLETE);
    }
}
