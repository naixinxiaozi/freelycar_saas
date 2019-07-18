package com.freelycar.saas.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tangwei - Toby
 * @date 2019-06-24
 * @email toby911115@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryOrder {
    private String id;

    private String licensePlate;

    private String carColor;

    private String carImageUrl;

    private String carBrand;

    private String payState;
}
