package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.Constants;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.exception.ArgumentMissingException;
import com.freelycar.saas.exception.ObjectNotFoundException;
import com.freelycar.saas.jwt.TokenAuthenticationUtil;
import com.freelycar.saas.project.entity.Employee;
import com.freelycar.saas.project.entity.Staff;
import com.freelycar.saas.project.repository.EmployeeRepository;
import com.freelycar.saas.project.repository.StaffRepository;
import com.freelycar.saas.project.repository.StoreRepository;
import com.freelycar.saas.util.UpdateTool;
import com.freelycar.saas.wechat.model.EmployeeInfo;
import com.freelycar.saas.wechat.model.WeChatEmployee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ConsumerOrderService consumerOrderService;

    public Employee modify(Employee employee) throws EntityNotFoundException {
        Employee source = employeeRepository.getOne(employee.getId());
        //将目标对象（projectType）中的null值，用源对象中的值替换
        UpdateTool.copyNullProperties(source, employee);
        return employeeRepository.saveAndFlush(employee);
    }

    public ResultJsonObject selectStore(Employee employee) {
        if (null == employee) {
            return ResultJsonObject.getErrorResult(null, "操作失败，参数对象employee为空");
        }
        String id = employee.getId();
        String defaultStoreId = employee.getDefaultStoreId();
        String defaultStaffId = employee.getDefaultStaffId();

        if (StringUtils.isEmpty(id)) {
            return ResultJsonObject.getErrorResult(null, "操作失败，参数对象employee中id为空");
        }
        if (StringUtils.isEmpty(defaultStoreId)) {
            return ResultJsonObject.getErrorResult(null, "操作失败，参数对象employee中defaultStoreId为空");
        }
        if (StringUtils.isEmpty(defaultStaffId)) {
            return ResultJsonObject.getErrorResult(null, "操作失败，参数对象employee中defaultStaffId为空");
        }
        try {
            modify(employee);
        } catch (EntityNotFoundException e) {
            logger.error(e.getMessage(), e);
            return ResultJsonObject.getErrorResult(null, "操作失败，未找到对应id的实体对象");
        }
        return ResultJsonObject.getDefaultResult(null);
    }

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
        List<Staff> staffList = null;
        try {
            staffList = getStaffs(account);
        } catch (ArgumentMissingException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        if (null == staffList || staffList.isEmpty()) {
            return ResultJsonObject.getErrorResult(null, "登录成功，但没有开通相关智能柜服务点权限，请联系管理人员");
        }

        String jwt = TokenAuthenticationUtil.generateAuthentication(employeeResult.getId());

        return ResultJsonObject.getDefaultResult(new WeChatEmployee(jwt, employeeResult, staffList));
    }

    /**
     * 根据手机号查询staff表中的有效数据
     *
     * @param phone
     * @return
     * @throws ArgumentMissingException
     */
    public List<Staff> getStaffs(String phone) throws ArgumentMissingException {
        if (StringUtils.isEmpty(phone)) {
            throw new ArgumentMissingException("手机号查询staff数据失败：参数phone为空值");
        }
        return staffRepository.findAllByPhoneAndDelStatusAndIsArk(phone, Constants.DelStatus.NORMAL.isValue(), true);
    }


    /**
     * 展示微信端雇员的详细信息
     *
     * @param id
     * @return
     * @throws ArgumentMissingException
     */
    public ResultJsonObject detail(String id) throws ArgumentMissingException, ObjectNotFoundException {
        Employee employee = this.findObjectById(id);

        String defaultStaffId = employee.getDefaultStaffId();
        String defaultStoreId = employee.getDefaultStoreId();

        if (StringUtils.isEmpty(defaultStaffId)) {
            throw new ArgumentMissingException("参数defaultStaffId缺失，无法查询相关信息");
        }
        if (StringUtils.isEmpty(defaultStoreId)) {
            throw new ArgumentMissingException("参数defaultStoreId缺失，无法查询相关信息");
        }
//        Staff staff = staffRepository.getOne(defaultStaffId);
//        Store store = storeRepository.getOne(defaultStoreId);

        EmployeeInfo employeeInfo = new EmployeeInfo();
        employeeInfo.setName(employee.getTrueName());
        employeeInfo.setCity(employee.getCity());
        employeeInfo.setProvince(employee.getProvince());
        employeeInfo.setNotification(employee.getNotification());
        employeeInfo.setHeadImgUrl(employee.getHeadImgUrl());
        employeeInfo.setGender(employee.getGender());

        //查询历史订单条数
        int res = 0;
        try {
            res = consumerOrderService.getStaffOrderServiced(defaultStaffId);
        } catch (ArgumentMissingException e) {
            logger.error(e.getMessage(), e);
        }

        employeeInfo.setHistoryOrderCount(res);


        return ResultJsonObject.getDefaultResult(employeeInfo);
    }

    /**
     * 切换服务状态
     *
     * @param id
     * @return
     * @throws ArgumentMissingException
     */
    public ResultJsonObject switchServiceStatus(String id) throws ArgumentMissingException, ObjectNotFoundException {
        Employee employee = this.findObjectById(id);
        boolean notification = employee.getNotification();
        //置为相反状态
        employee.setNotification(!notification);
        try {
            employeeRepository.save(employee);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return ResultJsonObject.getErrorResult(null);
        }
        return ResultJsonObject.getDefaultResult(employee);
    }

    Employee findObjectById(String id) throws ArgumentMissingException, ObjectNotFoundException {
        if (StringUtils.isEmpty(id)) {
            throw new ArgumentMissingException("参数id为空值");
        }
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (!employeeOptional.isPresent()) {
            throw new ObjectNotFoundException("未找到id为：" + id + "的employee数据");
        }
        return employeeOptional.get();
    }

}
