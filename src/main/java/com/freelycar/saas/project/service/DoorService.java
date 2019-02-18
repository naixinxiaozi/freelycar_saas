package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.Constants;
import com.freelycar.saas.exception.ArgumentMissingException;
import com.freelycar.saas.exception.NoEmptyArkException;
import com.freelycar.saas.project.entity.Door;
import com.freelycar.saas.project.repository.DoorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Random;

/**
 * @author tangwei - Toby
 * @date 2019-02-18
 * @email toby911115@gmail.com
 */
@Service
public class DoorService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DoorRepository doorRepository;

    /**
     * 获取一个随机的可使用的柜门
     *
     * @param arkSn
     * @return
     * @throws NoEmptyArkException
     * @throws ArgumentMissingException
     */
    public Door getUsefulDoor(String arkSn) throws NoEmptyArkException, ArgumentMissingException {
        if (StringUtils.isEmpty(arkSn)) {
            throw new ArgumentMissingException("参数arkSn缺失");
        }
        List<Door> emptyDoorList = doorRepository.findByArkSnAndStateAndDelStatus(arkSn, Constants.DoorState.EMPTY.getValue(), Constants.DelStatus.NORMAL.isValue());
        if (null == emptyDoorList || emptyDoorList.isEmpty()) {
            logger.error("没有可分配的智能柜！");
            throw new NoEmptyArkException("没有查找到可使用的空智能柜");
        }
        int targetIndex;
        int emptyDoorsCount = emptyDoorList.size();
        if (1 == emptyDoorsCount) {
            targetIndex = 0;
        } else {
            //获取一个随机下标
            Random random = new Random();
            targetIndex = random.nextInt(emptyDoorsCount);
        }
        return emptyDoorList.get(targetIndex);
    }
}
