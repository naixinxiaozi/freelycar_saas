package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.Constants;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.Employee;
import com.freelycar.saas.project.entity.Staff;
import com.freelycar.saas.project.repository.EmployeeRepository;
import com.freelycar.saas.project.repository.StaffRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2019-06-17
 * @email toby911115@gmail.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class EmployeeService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private StaffRepository staffRepository;

    /**
     * 雇员登录方法（微信端技师登录方法）
     *
     * @param employee
     * @return
     */
    public ResultJsonObject login(Employee employee) {
        String account = employee.getAccount();
        String password = employee.getPassword();
        String openId = employee.getOpenId();

        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(password)) {
            return ResultJsonObject.getErrorResult(null, "登录失败：接收到的参数中，用户名或密码为空");
        }
        if (StringUtils.isEmpty(openId)) {
            return ResultJsonObject.getErrorResult(null, "登录失败：接收到的参数中，openId为空。注意：这会影响消息推送");
        }

        // 查询是否有这个账号，没有的话直接返回登录失败
        Employee employeeResult = employeeRepository.findTopByAccountAndPasswordAndDelStatus(account, password, Constants.DelStatus.NORMAL.isValue());
        if (null == employeeResult) {
            return ResultJsonObject.getErrorResult(null, "不存在有效的账号，请核实后重新登录");
        }

        //更新微信的相关数据：头像、性别、省份、城市、昵称
        String headImgUrl = employee.getHeadImgUrl();
        String nickName = employee.getNickName();
        String gender = employee.getGender();
        String province = employee.getProvince();
        String city = employee.getCity();

        if (StringUtils.hasText(headImgUrl)) {
            employeeResult.setHeadImgUrl(headImgUrl);
        }
        if (StringUtils.hasText(nickName)) {
            employeeResult.setNickName(nickName);
        }
        if (StringUtils.hasText(gender)) {
            employeeResult.setGender(gender);
        }
        if (StringUtils.hasText(province)) {
            employeeResult.setProvince(province);
        }
        if (StringUtils.hasText(city)) {
            employeeResult.setCity(city);
        }

        employeeRepository.save(employeeResult);


        //查询staff表中有几个对应的数据，列举出来供用户选择门店
        List<Staff> staffList = staffRepository.findAllByPhoneAndDelStatus(account, Constants.DelStatus.NORMAL.isValue());
        if (null == staffList || staffList.isEmpty()) {
            return ResultJsonObject.getErrorResult(null, "登录成功，但没有开通相关智能柜服务点权限，请联系管理人员");
        }

        return ResultJsonObject.getDefaultResult(staffList);
    }
}
