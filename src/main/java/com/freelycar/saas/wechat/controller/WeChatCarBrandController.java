package com.freelycar.saas.wechat.controller;

import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.service.CarBrandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tangwei - Toby
 * @date 2019-01-04
 * @email toby911115@gmail.com
 */
@RestController
@RequestMapping("/wechat/carBrand")
public class WeChatCarBrandController {
    private static Logger logger = LoggerFactory.getLogger(WeChatCarBrandController.class);

    @Autowired
    private CarBrandService carBrandService;

    @GetMapping("/init")
    public ResultJsonObject initBrandData() {
        carBrandService.initCarBrand();
        return ResultJsonObject.getDefaultResult("执行完毕，没有错误，请检查数据库！");
    }

    @GetMapping("/getAllCarBrand")
    public ResultJsonObject getAllCarBrand() {
        return ResultJsonObject.getDefaultResult(carBrandService.getAllCarBrand());
    }

    @GetMapping("/getCarBrandByPinyin")
    public ResultJsonObject getCarBrandByPinyin(@RequestParam String pinyin) {
        if (StringUtils.isEmpty(pinyin)) {
            return ResultJsonObject.getErrorResult(null);
        }
        return ResultJsonObject.getDefaultResult(carBrandService.getCarBrandByPinyin(pinyin));
    }

    @GetMapping("/getCarTypeByCarBrandId")
    public ResultJsonObject getCarTypeByCarBrandId(@RequestParam Integer carBrandId) {
        if (null == carBrandId) {
            return ResultJsonObject.getErrorResult(null);
        }
        return ResultJsonObject.getDefaultResult(carBrandService.getCarTypeByCarBrandId(carBrandId));
    }

    @GetMapping("/getCarModelByCarTypeId")
    public ResultJsonObject getCarModelByCarTypeId(@RequestParam Integer carTypeId) {
        if (null == carTypeId) {
            return ResultJsonObject.getErrorResult(null);
        }
        return ResultJsonObject.getDefaultResult(carBrandService.getCarModelByCarTypeId(carTypeId));
    }


    @GetMapping("/getCarBrandByKeyword")
    public ResultJsonObject getCarBrandByKeyword(@RequestParam String keyword) {
        if (StringUtils.isEmpty(keyword)) {
            return ResultJsonObject.getErrorResult(null);
        }
        return ResultJsonObject.getDefaultResult(carBrandService.getCarBrandByKeyword(keyword));
    }

    @GetMapping("/getHotCarBrand")
    public ResultJsonObject getHotCarBrand() {
        return ResultJsonObject.getDefaultResult(carBrandService.getHotCarBrand());
    }
}
