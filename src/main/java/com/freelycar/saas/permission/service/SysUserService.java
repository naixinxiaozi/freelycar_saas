package com.freelycar.saas.permission.service;

import com.freelycar.saas.basic.wrapper.Constants;
import com.freelycar.saas.basic.wrapper.ResultCode;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.permission.entity.SysUser;
import com.freelycar.saas.permission.repository.SysUserRepository;
import com.freelycar.saas.util.UpdateTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2019-01-18
 * @email toby911115@gmail.com
 */
@Service
public class SysUserService {

    @Autowired
    private SysUserRepository sysUserRepository;

    /**
     * 登录验证
     *
     * @param userName
     * @param password
     * @return
     */
    public SysUser login(String userName, String password) {
        return sysUserRepository.findByDelStatusAndUsernameAndPassword(Constants.DelStatus.NORMAL.isValue(), userName, password);
    }

    /**
     * 保存
     *
     * @param sysUser
     * @return
     */
    private SysUser saveOrUpdate(SysUser sysUser) {
        if (null == sysUser) {
            return null;
        }
        Long id = sysUser.getId();
        if (null == id) {
            sysUser.setCreateTime(new Timestamp(System.currentTimeMillis()));
            sysUser.setDelStatus(Constants.DelStatus.NORMAL.isValue());
            sysUser.setRoles(new ArrayList<>());
        } else {
            SysUser source = sysUserRepository.getOne(id);
            UpdateTool.copyNullProperties(source, sysUser);
        }
        return sysUserRepository.save(sysUser);
    }


    /**
     * 用户名验重（所有的用户名都不可以重复）
     *
     * @param sysUser
     * @return
     */
    private boolean checkRepeat(SysUser sysUser) {
        Long id = sysUser.getId();
        String userName = sysUser.getUsername();

        List<SysUser> sysUserList;
        if (null != id) {
            sysUserList = sysUserRepository.findByUsernameAndDelStatusAndIdNot(userName, Constants.DelStatus.NORMAL.isValue(), id);
        } else {
            sysUserList = sysUserRepository.findByUsernameAndDelStatus(userName, Constants.DelStatus.NORMAL.isValue());
        }
        return !sysUserList.isEmpty();
    }


    /**
     * 新增/修改
     *
     * @param sysUser
     * @return
     */
    public ResultJsonObject addOrModify(SysUser sysUser) {
        if (null == sysUser) {
            return ResultJsonObject.getErrorResult(null, "保存失败，sysUser" + ResultCode.PARAM_NOT_COMPLETE.message());
        }
        if (this.checkRepeat(sysUser)) {
            return ResultJsonObject.getErrorResult(null, "保存失败，用户名：" + sysUser.getUsername() + ResultCode.USER_HAS_EXISTED.message());
        }
        SysUser res = this.saveOrUpdate(sysUser);
        if (null == res) {
            return ResultJsonObject.getErrorResult(null, "保存失败，" + ResultCode.UNKNOWN_ERROR.message());
        }
        return ResultJsonObject.getDefaultResult(res.getId());
    }

    /**
     * 删除某个账号
     *
     * @param id
     * @return
     */
    public ResultJsonObject deleteById(long id) {
        int res = sysUserRepository.delById(id);
        if (res == 1) {
            return ResultJsonObject.getDefaultResult(id);
        }
        return ResultJsonObject.getErrorResult(id, "删除失败！");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    public ResultJsonObject delByIds(String ids) {
        if (StringUtils.isEmpty(ids)) {
            return ResultJsonObject.getErrorResult(null, "删除失败：ids" + ResultCode.PARAM_NOT_COMPLETE.message());
        }
        String[] idsList = ids.split(",");
        for (String idStr : idsList) {
            long id = Long.parseLong(idStr);
            sysUserRepository.delById(id);
        }
        return ResultJsonObject.getDefaultResult(null);
    }
}
