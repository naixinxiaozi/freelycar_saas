package com.freelycar.saas.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.freelycar.saas.iotcloudcn.ArkDevice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tangwei - Toby
 * @date 2019-02-12
 * @email toby911115@gmail.com
 */
@RestController
@RequestMapping("/iot/ark")
public class IotController {


    @PostMapping("/deviceStateChange")
    public JSONObject deviceStateChange(@RequestParam ArkDevice deviceState) {
        //TODO 接收到数据后进行数据推送等处理
        String deviceId = deviceState.getDeviceId();
        boolean online = deviceState.getOnline();

        System.out.println(deviceState);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "SUCCESS");
        return jsonObject;
    }

    @PostMapping("/boxClosed")
    public JSONObject boxClosed(@RequestParam ArkDevice arkDevice) {
        //TODO 接收到数据后进行数据推送等处理
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
