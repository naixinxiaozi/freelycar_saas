package com.freelycar.saas.wechat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author tangwei - Toby
 * @date 2019-03-11
 * @email toby911115@gmail.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardRecordInfo {
    private String id;

    private Timestamp createTime;

    private double price;

    private String projectName;
}
