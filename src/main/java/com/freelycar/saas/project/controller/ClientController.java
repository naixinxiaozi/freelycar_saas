package com.freelycar.saas.project.controller;

import com.freelycar.saas.aop.LoggerManage;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.exception.CarNumberValidationException;
import com.freelycar.saas.project.entity.Client;
import com.freelycar.saas.project.model.NewClientInfo;
import com.freelycar.saas.project.service.ClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tangwei - Toby
 * @date 2018-12-25
 * @email toby911115@gmail.com
 */
@Api(value = "门店客户管理", description = "门店客户管理接口", tags = "门店客户管理接口")
@RestController
@RequestMapping("/client")
public class ClientController {
    private Logger logger = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    private ClientService clientService;

    @ApiOperation(value = "新增客户（提交数据中包含车辆和客户信息）", produces = "application/json")
    @PostMapping("/addClientAndCar")
    @LoggerManage(description = "新增客户（提交数据中包含车辆和客户信息）")
    public ResultJsonObject addClientAndCar(@RequestBody NewClientInfo newClientInfo) {
        try {
            return clientService.addClientAndCar(newClientInfo.getClient(), newClientInfo.getCar());
        } catch (CarNumberValidationException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultJsonObject.getErrorResult(null, e.getMessage());
        }
    }

    /**
     * 新增/修改客户
     *
     * @param client
     * @return
     */
    @ApiOperation(value = "新增/修改客户", produces = "application/json")
    @PostMapping("/modify")
    @LoggerManage(description = "新增/修改客户")
    public ResultJsonObject modify(@RequestBody Client client) {
        Client clientRes = clientService.saveOrUpdate(client);
        if (null != clientRes) {
            return ResultJsonObject.getDefaultResult(clientRes);
        }
        return ResultJsonObject.getErrorResult(null, "保存失败！");
    }


    /**
     * 获取客户基本信息（不包含会员卡、车辆等）
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "获取客户基本信息（不包含会员卡、车辆等）", produces = "application/json")
    @GetMapping(value = "/detail")
    @LoggerManage(description = "调用方法：获取客户基本信息（不包含会员卡、车辆等）")
    public ResultJsonObject detail(@RequestParam String id) {
        return clientService.getDetail(id);
    }

    /**
     * 获取客户基本信息（包含会员卡、车辆等）
     */
    @ApiOperation(value = "获取客户基本信息（包含会员卡、车辆）", produces = "application/json")
    @GetMapping(value = "/getCustomerInfo")
    @LoggerManage(description = "调用方法：获取客户基本信息（包含会员卡、车辆）")
    public ResultJsonObject getCustomerInfo(@RequestParam String id) {
        return clientService.getCustomerInfo(id);
    }

    /**
     * 获取客户列表
     *
     * @param storeId
     * @param currentPage
     * @param pageSize
     * @param name
     * @param phone
     * @return
     */
    @ApiOperation(value = "获取客户列表（分页）", produces = "application/json")
    @GetMapping(value = "/list")
    @LoggerManage(description = "调用方法：获取客户列表")
    public ResultJsonObject list(
            @RequestParam String storeId,
            @RequestParam Integer currentPage,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Boolean isMember,
            @RequestParam(required = false) String licensePlate

    ) {
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.hasText(StringUtils.trimWhitespace(name))) {
            params.put("name", name);
        }
        if (StringUtils.hasText(StringUtils.trimWhitespace(phone))) {
            params.put("phone", phone);
        }
        if (null != isMember) {
            params.put("isMember", isMember);
        }
        if (StringUtils.hasText(StringUtils.trimWhitespace(phone))) {
            params.put("licensePlate", licensePlate);
        }
        return clientService.list(storeId, currentPage, pageSize, params);
    }

    @ApiOperation(value = "会员统计", produces = "application/json")
    @GetMapping(value = "/memberStatistics")
    @LoggerManage(description = "调用方法：会员统计")
    public ResultJsonObject memberStatistics(
            @RequestParam String storeId
    ) {
        return ResultJsonObject.getDefaultResult(clientService.memberStatistics(storeId));
    }

    @ApiOperation(value = "删除单个客户信息", produces = "application/json")
    @GetMapping(value = "/delete")
    @LoggerManage(description = "删除单个客户信息")
    public ResultJsonObject delete(@RequestParam String id) {
        return clientService.delete(id);
    }

}
