package com.freelycar.saas.project.controller;

import com.freelycar.saas.aop.LoggerManage;
import com.freelycar.saas.project.entity.WxUserInfo;
import com.freelycar.saas.project.repository.WxUserInfoRepository;
import com.freelycar.saas.project.service.WxUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tangwei
 * @date 2018/9/25
 */
@RestController
@RequestMapping("/wxUserInfo")
public class WxUserInfoController {
    @Autowired
    private WxUserInfoService wxUserInfoService;

    @Autowired
    private WxUserInfoRepository wxUserInfoRepository;

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @LoggerManage(description = "微信用户信息新增")
    public Map<String,Object> save(@RequestBody WxUserInfo wxUserInfo) {
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("status","success");
        resultMap.put("content",wxUserInfoRepository.save(wxUserInfo));
        return resultMap;
    }

    @RequestMapping(value = "/findById",method = RequestMethod.GET)
    @LoggerManage(description = "通过id查找微信用户信息")
    public Map<String,Object> findById(String id) {
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("status","success");
        resultMap.put("content",wxUserInfoRepository.findWxUserInfoById(id));
        return resultMap;
    }

    @RequestMapping(value = "/findAllUsefulInfo",method = RequestMethod.GET)
    @LoggerManage(description = "查询出所有有效的微信用户信息")
    public Map<String,Object> findAllUsefulInfo() {
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("status","success");
        resultMap.put("content",wxUserInfoRepository.findAllByDelStatusOrderByAddTimeDesc(0));
        return resultMap;
    }
}
