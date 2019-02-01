package com.freelycar.saas.wechat.model;

import com.freelycar.saas.project.entity.Car;
import com.freelycar.saas.project.entity.WxUserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2019-01-07
 * @email toby911115@gmail.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalInfo {
    private WxUserInfo wxUserInfo;

    private List<Car> cars;

    private Float cardBalance;
}
