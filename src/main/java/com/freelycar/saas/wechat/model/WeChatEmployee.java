package com.freelycar.saas.wechat.model;

import com.freelycar.saas.project.entity.Employee;
import com.freelycar.saas.project.entity.Staff;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2019-06-19
 * @email toby911115@gmail.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeChatEmployee {
    private String jwt;

    private Employee employee;

    private List<Staff> staffList;
}
