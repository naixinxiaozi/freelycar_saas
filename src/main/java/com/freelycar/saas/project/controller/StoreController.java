package com.freelycar.saas.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.freelycar.saas.basic.wrapper.PaginationRJO;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.Store;
import com.freelycar.saas.project.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author tangwei - Toby
 * @date 2019-01-07
 * @email toby911115@gmail.com
 */
@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private StoreService storeService;


    /**
     * 新增门店
     *
     * @param store
     * @return
     */
    @PostMapping("/add")
    public ResultJsonObject add(@RequestBody Store store) {
        Store storeRes = storeService.saveOrUpdate(store);
        if (null == storeRes) {
            return ResultJsonObject.getErrorResult(null, "保存失败");
        }
        return ResultJsonObject.getDefaultResult(storeRes);
    }


    /**
     * 删除门店
     *
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public ResultJsonObject delete(String id) {
        return storeService.delete(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @PostMapping("/batchDelete")
    public ResultJsonObject batchDelete(@RequestBody JSONObject ids) {
        if (null == ids) {
            return ResultJsonObject.getErrorResult(null, "ids参数为NULL");
        }
        return storeService.delByIds(ids.getString("ids"));
    }

    /**
     * 查询
     *
     * @param name
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public ResultJsonObject list(
            @RequestParam(required = false) String name,
            @RequestParam Integer currentPage,
            @RequestParam(required = false) Integer pageSize) {
        if (StringUtils.isEmpty(name)) {
            name = "";
        }
        return ResultJsonObject.getDefaultResult(PaginationRJO.of(storeService.list(name, currentPage, pageSize)));
    }
}
