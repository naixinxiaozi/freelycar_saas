package com.freelycar.saas.project.controller;

import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.Client;
import com.freelycar.saas.project.model.NewClientInfo;
import com.freelycar.saas.project.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ClientService clientService;

    @PostMapping("/addClientAndCar")
    public ResultJsonObject addClientAndCar(@RequestBody NewClientInfo newClientInfo) {
        return clientService.addClientAndCar(newClientInfo.getClient(), newClientInfo.getCar());
    }

    @PostMapping("/modify")
    public ResultJsonObject modify(@RequestBody Client client) {
        Client clientRes = clientService.saveOrUpdate(client);
        if (null != clientRes) {
            return ResultJsonObject.getDefaultResult(clientRes);
        }
        return ResultJsonObject.getErrorResult(null, "保存失败！");
    }
}
