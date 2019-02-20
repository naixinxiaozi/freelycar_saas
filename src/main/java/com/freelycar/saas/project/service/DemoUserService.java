package com.freelycar.saas.project.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class DemoUserService {
    public String getUserByMobile(String mobile) {
        return "调用了getUserByMobile方法！mobile:" + mobile;
    }
}
