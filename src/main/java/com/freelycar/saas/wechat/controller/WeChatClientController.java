package com.freelycar.saas.wechat.controller;

import com.freelycar.saas.aop.LoggerManage;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.controller.CarController;
import com.freelycar.saas.project.entity.Car;
import com.freelycar.saas.project.service.CarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author tangwei - Toby
 * @date 2019-01-23
 * @email toby911115@gmail.com
 */
@RestController
@RequestMapping("/wechat/client")
public class WeChatClientController {
    private Logger logger = LoggerFactory.getLogger(CarController.class);

    @Autowired
    private CarService carService;
    private String errorMsg;

    /**
     * 删除操作
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/deleteCar")
    @LoggerManage(description = "调用方法：微信端-删除车辆")
    public ResultJsonObject deleteCar(@RequestParam String id) {
        return carService.delete(id);
    }

    /**
     * 新增/修改车辆
     *
     * @param car
     * @return
     */

    @PostMapping(value = "/addCar")
    @LoggerManage(description = "调用方法：微信端-新增/修改车辆信息")
    public ResultJsonObject addCar(@RequestBody Car car) {
        if (null == car) {
            errorMsg = "接收到的JSON：car对象为NULL";
            logger.error(errorMsg);
            return ResultJsonObject.getErrorResult(null, errorMsg);
        }
        return carService.modify(car);
    }


}
