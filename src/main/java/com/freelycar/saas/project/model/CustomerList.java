package com.freelycar.saas.project.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigInteger;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerList {

    private String id;
    @Excel(name = "姓名", orderNum = "0")
    private String name;
    @Excel(name = "手机号码", orderNum = "1")
    private String phone;
    @Excel(name = "车牌号码", orderNum = "2")
    private String plates;
    @Excel(name = "品牌", orderNum = "3")
    private String brands;
    @Excel(name = "是否会员", orderNum = "4")
    private String isMember;
    @Excel(name = "消费次数", orderNum = "5")
    private BigInteger totalCount;
    @Excel(name = "最近到店时间", orderNum = "6")
    private Timestamp lastVisit;
    @Excel(name = "卡内未消费金额", orderNum = "7")
    private Double totalBalance;
}
