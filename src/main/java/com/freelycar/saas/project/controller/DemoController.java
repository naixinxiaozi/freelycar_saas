package com.freelycar.saas.project.controller;

import com.freelycar.saas.project.service.DemoService;
import com.freelycar.saas.project.service.DemoUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DemoController {
    @Autowired
    private DemoService demoService;

    @Autowired
    private DemoUserService demoCommonService;

    @GetMapping("/superadmin/index")
    public Map<String,Object> index() {
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("status","success");
        resultMap.put("content",demoService.getString());
        return resultMap;
    }

    @GetMapping(value = "/mobile/{mobile:.+}")
    public Object getSingleLoanItem(@PathVariable("mobile") String mobile) {
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("status","success");
        resultMap.put("content",demoCommonService.getUserByMobile(mobile));
        return resultMap;
    }
}