package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.DelStatus;
import com.freelycar.saas.basic.wrapper.PageableTools;
import com.freelycar.saas.basic.wrapper.PaginationRJO;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.Staff;
import com.freelycar.saas.project.repository.StaffRepository;
import com.freelycar.saas.util.UpdateTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static com.freelycar.saas.basic.wrapper.ResultCode.RESULT_DATA_NONE;

@Service
public class StaffService {
    private static Logger logger = LoggerFactory.getLogger(StaffService.class);

    @Autowired
    private StaffRepository staffRepository;

    /**
     * 新增/修改员工对象
     *
     * @param staff
     * @return
     */
    public ResultJsonObject modify(Staff staff) {
        try {
            //验重
            if (this.checkRepeatName(staff)) {
                return ResultJsonObject.getErrorResult(null, "已包含名称为：“" + staff.getName() + "”的数据，不能重复添加。");
            }

            //是否有ID，判断时新增还是修改
            String id = staff.getId();
            if (StringUtils.isEmpty(id)) {
                staff.setDelStatus(DelStatus.EFFECTIVE.isValue());
                staff.setCreateTime(new Timestamp(System.currentTimeMillis()));
            } else {
                Optional<Staff> optional = staffRepository.findById(id);
                //判断数据库中是否有该对象
                if (!optional.isPresent()) {
                    logger.error("修改失败，原因：" + Staff.class + "中不存在id为 " + id + " 的对象");
                    return ResultJsonObject.getErrorResult(null);
                }
                Staff source = optional.get();
                //将目标对象（projectType）中的null值，用源对象中的值替换
                UpdateTool.copyNullProperties(source, staff);
            }
            //执行保存/修改
            return ResultJsonObject.getDefaultResult(staffRepository.saveAndFlush(staff));
        } catch (Exception e) {
            return ResultJsonObject.getErrorResult(null);
        }
    }

    /**
     * 验证员工是否重复
     * true：重复；false：不重复
     *
     * @param staff
     * @return
     */
    private boolean checkRepeatName(Staff staff) {
        List<Staff> staffList;
        if (null != staff.getId()) {
            staffList = staffRepository.checkRepeatName(staff.getId(), staff.getName(), staff.getStoreId());
        } else {
            staffList = staffRepository.checkRepeatName(staff.getName(), staff.getStoreId());
        }
        return staffList.size() != 0;
    }

    /**
     * 获取员工详情
     *
     * @param id
     * @return
     */
    public ResultJsonObject getDetail(String id) {
        return ResultJsonObject.getDefaultResult(staffRepository.findById(id));
    }


    /**
     * 查询员工列表
     *
     * @param storeId
     * @param currentPage
     * @param pageSize
     * @return
     */
    public PaginationRJO list(String storeId, Integer currentPage, Integer pageSize, String id, String name) {
        logger.debug("storeId:" + storeId);
        Page<Staff> staffPage = staffRepository.findAllByDelStatusAndStoreIdAndIdContainingAndNameContaining(DelStatus.EFFECTIVE.isValue(), storeId, id, name, PageableTools.basicPage(currentPage, pageSize));
        return PaginationRJO.of(staffPage);
    }

    /**
     * 删除操作（软删除）
     *
     * @param id
     * @return
     */
    @Transactional
    public ResultJsonObject delete(String id) {
        try {
            int result = staffRepository.delById(id);
            if (result != 1) {
                return ResultJsonObject.getErrorResult(id, "删除失败," + RESULT_DATA_NONE);
            }
        } catch (Exception e) {
            return ResultJsonObject.getErrorResult(id, "删除失败，删除操作出现异常");
        }
        return ResultJsonObject.getDefaultResult(id, "删除成功");
    }


    /**
     * 智能柜技师开通
     *
     * @param id
     * @return
     */

    public ResultJsonObject openArk(String id, String account, String password) {
        Optional<Staff> optionalStaff=staffRepository.findById(id);
        if (optionalStaff.isPresent()){
            Staff staff = optionalStaff.get();
            staff.setAccount(account);
            staff.setPassword(password);
            staff.setArk(true);
            if (this.checkRepeatAccount(staff)) {
                return ResultJsonObject.getErrorResult(null, "已包含名称为：“" + staff.getAccount() + "”的账户，不能重复添加。");
            }

                return ResultJsonObject.getDefaultResult(staffRepository.saveAndFlush(staff));
        }
        return ResultJsonObject.getErrorResult(null,"id:"+id+"不存在！");


    }

    private boolean checkRepeatAccount(Staff staff) {
        List<Staff> staffList;
        if (null != staff.getId()) {
            staffList = staffRepository.checkRepeatAccount(staff.getAccount());
        } else {

            staffList = staffRepository.checkRepeatAccount(staff.getId(),staff.getAccount());
        }
        return staffList.size() != 0;
    }


    /**
     * 智能柜技师关闭
     *
     * @param id
     * @return
     */
    public ResultJsonObject closeArk(String id) {

        Optional<Staff> optionalStaff = staffRepository.findById(id);
        if (optionalStaff.isPresent()){
            Staff staff = optionalStaff.get();
            staff.setAccount(null);
            staff.setPassword(null);
            staff.setArk(false);

            return ResultJsonObject.getDefaultResult(staffRepository.saveAndFlush(staff));
        }
        return ResultJsonObject.getErrorResult(null,"id:"+id+"不存在！");

    }
}
