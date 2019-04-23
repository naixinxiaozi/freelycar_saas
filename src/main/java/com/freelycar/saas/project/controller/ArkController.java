package com.freelycar.saas.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.freelycar.saas.aop.LoggerManage;
import com.freelycar.saas.basic.wrapper.PaginationRJO;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.exception.ArgumentMissingException;
import com.freelycar.saas.exception.DataIsExistException;
import com.freelycar.saas.exception.DoorUsingException;
import com.freelycar.saas.exception.ObjectNotFoundException;
import com.freelycar.saas.project.entity.Ark;
import com.freelycar.saas.project.service.ArkService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author tangwei - Toby
 * @date 2019-02-12
 * @email toby911115@gmail.com
 */
@RestController
@RequestMapping("/ark")
public class ArkController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ArkService arkService;

    /**
     * 新增智能柜（含生成状态表数据）
     *
     * @param ark
     * @return
     */
    @ApiOperation(value = "新增智能柜（含生成状态表数据）", produces = "application/json")
    @PostMapping("/add")
    @LoggerManage(description = "调用接口：新增智能柜（含生成状态表数据）")
    public ResultJsonObject addArk(@RequestBody Ark ark) {
        try {
            return ResultJsonObject.getDefaultResult(arkService.addArk(ark));
        } catch (ArgumentMissingException | DataIsExistException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return ResultJsonObject.getErrorResult(null, e.getMessage());
        }
    }


    @ApiOperation(value = "修改智能柜）", produces = "application/json")
    @PostMapping("/modify")
    @LoggerManage(description = "调用接口：修改智能柜")
    public ResultJsonObject modifyArkInfo(@RequestBody Ark ark) {
        try {
            return ResultJsonObject.getDefaultResult(arkService.modify(ark));
        } catch (ArgumentMissingException | ObjectNotFoundException | DoorUsingException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return ResultJsonObject.getErrorResult(null, e.getMessage());
        }
    }

    @ApiOperation(value = "删除智能柜：物理删除，不可恢复数据", produces = "application/json")
    @GetMapping("/delete")
    @LoggerManage(description = "调用接口：删除智能柜")
    public ResultJsonObject deleteArk(@RequestParam String arkId) {
        try {
            arkService.delete(arkId);
            return ResultJsonObject.getDefaultResult("删除成功");
        } catch (ArgumentMissingException | DoorUsingException | ObjectNotFoundException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return ResultJsonObject.getErrorResult(null, e.getMessage());
        }
    }

    @ApiOperation(value = "批量删除智能柜：物理删除，不可恢复数据", produces = "application/json")
    @PostMapping("/batchDelete")
    @LoggerManage(description = "调用接口：批量删除智能柜")
    public ResultJsonObject batchDelete(@RequestBody JSONObject ids) {
        if (null == ids) {
            return ResultJsonObject.getErrorResult(null, "ids参数为NULL");
        }
        try {
            return arkService.batchDelete(ids.getString("ids"));
        } catch (ArgumentMissingException | ObjectNotFoundException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return ResultJsonObject.getErrorResult(null, e.getMessage());
        }
    }

    @ApiOperation(value = "获取智能柜列表（分页）", produces = "application/json")
    @GetMapping(value = "/list")
    @LoggerManage(description = "调用方法：获取智能柜列表")
    public ResultJsonObject list(
            @RequestParam Integer currentPage,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String storeId,
            @RequestParam(required = false) String arkSn
    ) {
        if (StringUtils.isEmpty(StringUtils.trimWhitespace(arkSn))) {
            arkSn = "";
        }
        return ResultJsonObject.getDefaultResult(PaginationRJO.of(arkService.list(storeId, currentPage, pageSize, arkSn)));
    }

}
