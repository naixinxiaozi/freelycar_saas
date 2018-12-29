package com.freelycar.saas.project.controller;

import com.freelycar.saas.aop.LoggerManage;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.Client;
import com.freelycar.saas.project.model.NewClientInfo;
import com.freelycar.saas.project.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author tangwei - Toby
 * @date 2018-12-25
 * @email toby911115@gmail.com
 */
@RestController
@RequestMapping("/client")
public class ClientController {

    private static Logger logger = LoggerFactory.getLogger(ClientController.class);
    @Autowired
    private ClientService clientService;
    private String errorMsg;

    @PostMapping("/addClientAndCar")
    public ResultJsonObject addClientAndCar(@RequestBody NewClientInfo newClientInfo) {
        return clientService.addClientAndCar(newClientInfo.getClient(), newClientInfo.getCar());
    }

    /**
     * 新增/修改客户
     * @param client
     * @return
     */
    @PostMapping("/modify")
    public ResultJsonObject modify(@RequestBody Client client) {
        Client clientRes = clientService.saveOrUpdate(client);
        if (null != clientRes) {
            return ResultJsonObject.getDefaultResult(clientRes);
        }
        return ResultJsonObject.getErrorResult(null, "保存失败！");
    }


    /**
     * 获取客户基本信息（不包含会员卡、车辆等）
     * @param id
     * @return
     */
    @GetMapping(value = "/detail")
    @LoggerManage(description = "调用方法：获取客户基本信息（不包含会员卡、车辆等）")
    public ResultJsonObject detail(@RequestParam String id) {

        if (null == id) {
            errorMsg = "接收到的参数：id为NULL";
            logger.error(errorMsg);
            return ResultJsonObject.getErrorResult(null, errorMsg);
        }
        return clientService.getDetail(id);
    }

    /**
     * 获取客户基本信息（包含会员卡、车辆等）
     *
     */
    @GetMapping(value = "/getCustomerInfo")
    @LoggerManage(description = "调用方法：获取客户基本信息（包含会员卡、车辆）")
    public ResultJsonObject getCustomerInfo(@RequestParam String id) {

        if (null == id) {
            errorMsg = "接收到的参数：id为NULL";
            logger.error(errorMsg);
            return ResultJsonObject.getErrorResult(null, errorMsg);
        }
        return clientService.getCustomerInfo(id);
    }
}
