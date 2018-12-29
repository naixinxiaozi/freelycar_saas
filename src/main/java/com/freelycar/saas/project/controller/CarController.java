package com.freelycar.saas.project.controller;

import com.freelycar.saas.aop.LoggerManage;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.Car;
import com.freelycar.saas.project.service.CarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/car")
public class CarController {
    private static Logger logger = LoggerFactory.getLogger(CarController.class);

    @Autowired
    private CarService carService;
    private String errorMsg;

    /**
     * 删除操作
     * @param id
     * @return
     */
    @GetMapping(value = "/delete")
    @LoggerManage(description = "调用方法：删除车辆")
    public ResultJsonObject delete(@RequestParam String id) {
        return carService.delete(id);
    }

    /**
     * 新增/修改车辆
     * @param car
     * @return
     * */

    @PostMapping(value = "/modify")
    @LoggerManage(description = "调用方法：新增/修改车辆信息")
    public ResultJsonObject saveOrUpdate(@RequestBody Car car) {
        if (null == car) {
            errorMsg = "接收到的参数：car为NULL";
            logger.error(errorMsg);
            return ResultJsonObject.getErrorResult(null, errorMsg);
        }
        return carService.modify(car);
    }

}
