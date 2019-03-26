package com.freelycar.saas.project.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author tangwei - Toby
 * @date 2019-03-26
 * @email toby911115@gmail.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderParticulars {
    private String id;

    @Excel(name = "车型", orderNum = "0")
    private String carBrand;

    @Excel(name = "车牌号码", orderNum = "1")
    private String licensePlate;

    @Excel(name = "车主姓名", orderNum = "2")
    private String clientName;

    @Excel(name = "联系方式", orderNum = "3")
    private String phone;

    @Excel(name = "消费项目", orderNum = "4")
    private String projectNames;

    @Excel(name = "金额", orderNum = "5")
    private Double cost;

    @Excel(name = "时间", exportFormat = "yyyy-MM-dd HH:mm:ss", orderNum = "6")
    private Date serviceTime;

    @Excel(name = "是否会员", orderNum = "7")
    private String isMember;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"")
                .append(id).append('\"');
        sb.append(",\"carBrand\":\"")
                .append(carBrand).append('\"');
        sb.append(",\"licensePlate\":\"")
                .append(licensePlate).append('\"');
        sb.append(",\"clientName\":\"")
                .append(clientName).append('\"');
        sb.append(",\"phone\":\"")
                .append(phone).append('\"');
        sb.append(",\"projectNames\":\"")
                .append(projectNames).append('\"');
        sb.append(",\"cost\":")
                .append(cost);
        sb.append(",\"serviceTime\":\"")
                .append(serviceTime).append('\"');
        sb.append(",\"isMember\":\"")
                .append(isMember).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
