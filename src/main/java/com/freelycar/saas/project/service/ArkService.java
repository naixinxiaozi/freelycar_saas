package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.*;
import com.freelycar.saas.exception.ArgumentMissingException;
import com.freelycar.saas.exception.DataIsExistException;
import com.freelycar.saas.exception.DoorUsingException;
import com.freelycar.saas.exception.ObjectNotFoundException;
import com.freelycar.saas.project.entity.Ark;
import com.freelycar.saas.project.repository.ArkRepository;
import com.freelycar.saas.util.UpdateTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2019-02-18
 * @email toby911115@gmail.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ArkService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

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
     * 1.子表数据中不存在“占用”的数据
     * 2.子表的数据会删除并重新生成
     *
     * @param ark
     * @return
     */
    public Ark modify(Ark ark) throws ArgumentMissingException, ObjectNotFoundException, DoorUsingException {
        if (null == ark) {
            throw new ArgumentMissingException("ark值为空");
        }
        String id = ark.getId();
        //新增
        if (StringUtils.isEmpty(id)) {
            throw new ArgumentMissingException("arkId为空值，无法修改");
        }

        //修改
        Ark source = arkRepository.findById(id).orElse(null);
        if (null == source) {
            throw new ObjectNotFoundException("未找到id为：" + id + " 的ark对象");
        }

        //更新主表数据
        UpdateTool.copyNullProperties(source, ark);

        Ark result = arkRepository.save(ark);

        //处理子表数据：
        if (doorService.isDoorUsing(id)) {
            throw new DoorUsingException("该智能柜存在正在使用的格子，暂时无法修改，请确认智能柜不被占用时再更改");
        }
        //删除重新生成
        doorService.deleteAllByArkId(id);
        doorService.generateDoors(result);


        return result;
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

    public void delete(String arkId) throws ArgumentMissingException, ObjectNotFoundException, DoorUsingException {
        if (StringUtils.isEmpty(arkId)) {
            throw new ArgumentMissingException("参数arkId为空值，无法执行删除操作");
        }
        if (doorService.isDoorUsing(arkId)) {
            throw new DoorUsingException("智能柜：" + arkId + "存在正在使用的格子，暂时无法删除，请确认智能柜不被占用时再删除");
        }
        doorService.deleteAllByArkId(arkId);
        arkRepository.deleteById(arkId);
    }

    public ResultJsonObject batchDelete(String ids) throws ArgumentMissingException, ObjectNotFoundException {
        if (StringUtils.isEmpty(ids)) {
            throw new ArgumentMissingException("参数ids为空值，无法执行批量删除智能柜操作");
        }

        //遍历所有id，未占用的删除，占用的放入列表中返回给前端
        List<String> doorUsingArkSnList = new ArrayList<>();
        String[] idsList = ids.split(",");
        for (String id : idsList) {
            try {
                delete(id);
            } catch (DoorUsingException e) {
                arkRepository.findById(id).ifPresent(ark -> doorUsingArkSnList.add(ark.getSn()));
            }
        }

        String sucMsg = "删除操作执行成功。";
        if (doorUsingArkSnList.size() > 0) {
            sucMsg += "由于智能柜：" + doorUsingArkSnList.toString() + " 有在使用格口，暂时不能删除。";
        }
        return ResultJsonObject.getDefaultResult(sucMsg);
    }

    public Page<Ark> list(String storeId, Integer currentPage, Integer pageSize, String arkSn) {
        Pageable pageable = PageableTools.basicPage(currentPage, pageSize, new SortDto("asc", "createTime"));
        if (StringUtils.hasText(storeId)) {
            return arkRepository.findAllByStoreIdAndSnContainingAndDelStatus(storeId, arkSn, Constants.DelStatus.NORMAL.isValue(), pageable);
        }
        return arkRepository.findAllBySnContainingAndDelStatus(arkSn, Constants.DelStatus.NORMAL.isValue(), pageable);
    }
}
