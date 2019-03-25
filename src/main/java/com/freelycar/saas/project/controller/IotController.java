package com.freelycar.saas.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.freelycar.saas.project.model.ArkDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tangwei - Toby
 * @date 2019-02-12
 * @email toby911115@gmail.com
 */
@RestController
@RequestMapping("/iot/ark")
public class IotController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 接收设备状态变化
     *
     * @param deviceState
     * @return
     */
    @PostMapping("/deviceStateChange")
    public JSONObject deviceStateChange(@RequestBody ArkDevice deviceState) {
        logger.info("接收到智能柜离线推送信息：");
        logger.info(deviceState.toString());
        // 接收到数据后进行数据推送等处理
        String deviceId = deviceState.getDeviceId();
        boolean online = deviceState.getOnline();



        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "SUCCESS");
        return jsonObject;
    }

    /**
     * 接收柜门关闭（已作废）
     * @param arkDevice
     * @return
     */
    @Deprecated
    @PostMapping("/boxClosed")
    public JSONObject boxClosed(@RequestBody ArkDevice arkDevice) {
        // 接收到数据后进行数据推送等处理
        String deviceId = arkDevice.getDeviceId();
        String boardId = arkDevice.getBoardId();
        int boxId = arkDevice.getBoxId();

        System.out.println(arkDevice);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "SUCCESS");
        return jsonObject;
    }

}
