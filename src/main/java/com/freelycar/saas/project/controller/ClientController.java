package com.freelycar.saas.project.controller;

import com.freelycar.saas.project.model.NewClientInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tangwei - Toby
 * @date 2018-12-25
 * @email toby911115@gmail.com
 */
@RestController
@RequestMapping("/client")
public class ClientController {

    @PostMapping("/add")
    public void addClientAndCar(@RequestBody NewClientInfo newClientInfo) {
        System.out.println(newClientInfo);
    }
}
