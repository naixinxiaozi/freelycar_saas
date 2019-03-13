package com.freelycar.saas.project.model;

import com.freelycar.saas.project.entity.Store;
import com.freelycar.saas.project.entity.StoreImg;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author tangwei - Toby
 * @date 2019-03-13
 * @email toby911115@gmail.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreInfo {
    private Store store;

    private List<String> storeImgIds;

    private List<StoreImg> storeImgs;
}
