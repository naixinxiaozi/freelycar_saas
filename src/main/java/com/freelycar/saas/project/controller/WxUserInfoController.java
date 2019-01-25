package com.freelycar.saas.project.controller;


import com.freelycar.saas.project.repository.WxUserInfoRepository;
import com.freelycar.saas.project.service.WxUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
