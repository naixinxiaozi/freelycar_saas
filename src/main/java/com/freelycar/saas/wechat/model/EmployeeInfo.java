package com.freelycar.saas.wechat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tangwei - Toby
 * @date 2019-06-19
 * @email toby911115@gmail.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeInfo {
    private String name;

    private String gender;

    private String headImgUrl;

    private String city;

    private String province;

    private Boolean notification;

    private int historyOrderCount;
}
