package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.Car;
import com.freelycar.saas.project.entity.Client;
import com.freelycar.saas.project.repository.ClientRepository;
import com.freelycar.saas.util.UpdateTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Optional;

/**
 * @author tangwei - Toby
 * @date 2018-12-25
 * @email toby911115@gmail.com
 */
@Service
@Transactional
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    public ResultJsonObject addClientAndCar(Client client, Car car) {
        //TODO 同时保存客户信息和车辆信息
        return null;
    }

    /**
     * 保存/修改客户信息
     *
     * @param client
     * @return
     */
    public Client saveOrUpdate(Client client) {
        if (null == client) {
            return null;
        }

        String id = client.getId();
        if (StringUtils.isEmpty(id)) {
            client.setCreateTime(new Timestamp(System.currentTimeMillis()));
            client.setDelStatus(false);
            client.setMember(false);
            client.setPoints(0);
            client.setState(0);
        } else {
            Optional<Client> optionalClient = clientRepository.findById(id);
            if (!optionalClient.isPresent()) {
                return null;
            }
            Client source = optionalClient.get();
            UpdateTool.copyNullProperties(source, client);
        }
        return clientRepository.saveAndFlush(client);
    }
}
