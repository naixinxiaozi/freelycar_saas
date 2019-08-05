package com.freelycar.saas.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tangwei - Toby
 * @date 2019-08-05
 * @email toby911115@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffInfo {
    private String staffId;

    private String staffName;

    private String employeeId;

    private String headImgUrl;

    private String phone;
}
