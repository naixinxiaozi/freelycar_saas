package com.freelycar.saas.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 快速开单时，根据车牌号显示的信息
 *
 * @author tangwei - Toby
 * @date 2019-01-30
 * @email toby911115@gmail.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderClientInfo {

    private String carId;

    private String licensePlate;

    private String carBrand;

    private String carType;

    private String storeId;

    private String lastMiles;

    private String clientId;

    private String clientName;

    private String phone;

    private Boolean isMember;

    private Double historyConsumption;

    private Double balance;

}
