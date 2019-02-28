package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.Constants;
import com.freelycar.saas.exception.ArgumentMissingException;
import com.freelycar.saas.exception.NoEmptyArkException;
import com.freelycar.saas.exception.OpenArkDoorFailedException;
import com.freelycar.saas.exception.OpenArkDoorTimeOutException;
import com.freelycar.saas.iotcloudcn.ArkOperation;
import com.freelycar.saas.iotcloudcn.util.ArkThread;
import com.freelycar.saas.iotcloudcn.util.BoxCommandResponse;
import com.freelycar.saas.project.entity.Ark;
import com.freelycar.saas.project.entity.Door;
import com.freelycar.saas.project.repository.DoorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

/**
 * @author tangwei - Toby
 * @date 2019-02-18
 * @email toby911115@gmail.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
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

    /**
     * 打开柜门并启用监控线程
     *
     * @param door
     * @throws ArgumentMissingException
     * @throws OpenArkDoorFailedException
     * @throws OpenArkDoorTimeOutException
     */
    public void openDoorByDoorObject(Door door) throws ArgumentMissingException, OpenArkDoorFailedException, OpenArkDoorTimeOutException {
        if (null == door) {
            throw new ArgumentMissingException("参数doorObject为空。");
        }
        String deviceId = door.getArkSn();
        int boxId = door.getDoorSn();

        if (StringUtils.isEmpty(deviceId)) {
            throw new ArgumentMissingException("参数doorObject中的arkSn值为空");
        }

        //打开柜门
        BoxCommandResponse boxCommandResponse = ArkOperation.openBox(deviceId, boxId);
        //判断是否成功，成功就启动监控线程
        if (null != boxCommandResponse && ArkOperation.SUCCESS_CODE == boxCommandResponse.code) {
            ArkThread arkThread = new ArkThread(deviceId, boxId);
            arkThread.start();

            //等待线程结束
            try {
                arkThread.join();
            } catch (InterruptedException e) {
                logger.error("捕获到等待线程中断异常……");
                e.printStackTrace();
            }

            String endStatus = arkThread.getEndStatus();
            //获取结果，如果不是success，说明超时
            if (!Constants.OPEN_SUCCESS.equalsIgnoreCase(endStatus)) {
                throw new OpenArkDoorTimeOutException("柜门关闭线程超时（1分钟）");
            }

            //如果正常到这边，不抛出异常，就说明一切正常，可以开单
        } else {
            throw new OpenArkDoorFailedException("打开柜门失败：从远端获取到打开柜门失败的信息。");
        }
    }

    /**
     * 生成智能柜的状态表数据（子表数据）
     *
     * @param ark
     * @throws ArgumentMissingException
     */
    public void generateDoors(Ark ark) throws ArgumentMissingException {
        if (null == ark) {
            throw new ArgumentMissingException("参数ark为空，生成door表数据失败");
        }
        String arkSn = ark.getSn();
        String arkId = ark.getId();
        int doorNum = ark.getDoorNum();
        if (StringUtils.isEmpty(arkSn)) {
            throw new ArgumentMissingException("参数arkSn为空，生成door表数据失败");
        }
        for (int i = 0; i < doorNum; i++) {
            Door door = new Door();
            door.setDelStatus(Constants.DelStatus.NORMAL.isValue());
            door.setCreateTime(new Timestamp(System.currentTimeMillis()));
            door.setState(Constants.DoorState.EMPTY.getValue());
            door.setArkId(arkId);
            door.setArkSn(arkSn);
            door.setDoorSn(i + 1);
            doorRepository.save(door);
        }
    }
}
