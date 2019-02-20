package com.freelycar.saas.project.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class DemoService {

    public String getString() {
        return "调用了getString方法！";
    }
}
