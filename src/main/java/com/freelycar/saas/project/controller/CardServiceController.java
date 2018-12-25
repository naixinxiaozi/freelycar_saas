package com.freelycar.saas.project.controller;

import com.freelycar.saas.aop.LoggerManage;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.CardService;
import com.freelycar.saas.project.service.CardServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cardService")
public class CardServiceController {
    private static Logger logger = LoggerFactory.getLogger(CardServiceController.class);
    @Autowired
    CardServiceService cardServiceService;
    private String errorMsg;

    /**
     * 新增/修改卡类
     *
     * @param cardService
     * @return
     */
    @PostMapping(value = "/modify")
    @LoggerManage(description = "调用方法：卡类新增/修改")
    public ResultJsonObject saveOrUpdate(@RequestBody CardService cardService) {
        if (null == cardService) {
            errorMsg = "接收到的参数：cardService为NULL";
            logger.error(errorMsg);
            return ResultJsonObject.getErrorResult(null, errorMsg);
        }
        return cardServiceService.modify(cardService);
    }

    /**
     * 获取卡类对象
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/detail")
    @LoggerManage(description = "调用方法：获取卡类详情")
    public ResultJsonObject detail(@RequestParam String id) {
        if (null == id) {
            errorMsg = "接收到的参数：id为NULL";
            logger.error(errorMsg);
            return ResultJsonObject.getErrorResult(null, errorMsg);
        }
        return cardServiceService.getDetail(id);
    }

    /**
     * 获取卡类列表
     *
     * @param storeId
     * @param currentPage
     * @return
     */
    @GetMapping(value = "/list")
    @LoggerManage(description = "调用方法：获取卡类列表")
    public ResultJsonObject list(
            @RequestParam String storeId,
            @RequestParam Integer currentPage,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String name
    ) {
        if (StringUtils.isEmpty(StringUtils.trimWhitespace(name))) {
            name = "";
        }
        return ResultJsonObject.getDefaultResult(cardServiceService.list(storeId, currentPage, pageSize, name));
    }

    /**
     * 删除操作（软删除）
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/delete")
    @LoggerManage(description = "调用方法：删除卡类信息")
    public ResultJsonObject delete(@RequestParam String id) {
        return cardServiceService.delete(id);
    }

    /**
     * 上架
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/upperShelf")
    @LoggerManage(description = "调用方法：上架卡类信息")
    public ResultJsonObject upperShelf(@RequestParam String id) {
        return cardServiceService.upperShelf(id);
    }

    /**
     * 下架
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/lowerShelf")
    @LoggerManage(description = "调用方法：下架卡类信息")
    public ResultJsonObject lowerShelf(@RequestParam String id) {
        return cardServiceService.lowerShelf(id);
    }



}
