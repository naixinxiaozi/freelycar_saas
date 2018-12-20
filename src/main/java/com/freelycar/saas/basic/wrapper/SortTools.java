package com.freelycar.saas.basic.wrapper;

import org.springframework.data.domain.Sort;

/**
 * 排序封装类
 * @author tangwei - Toby
 * @date 2018/11/29
 * @email toby911115@gmail.com
 */
public class SortTools {

    public static Sort basicSort() {
        return basicSort("desc", "createTime");
    }

    public static Sort basicSort(String orderType, String orderField) {
        Sort sort = new Sort(Sort.Direction.fromString(orderType), orderField);
        return sort;
    }

    public static Sort basicSort(SortDto... dtos) {
        Sort result = null;
        for(int i=0; i<dtos.length; i++) {
            SortDto dto = dtos[i];
            if(result == null) {
                result = new Sort(Sort.Direction.fromString(dto.getOrderType()), dto.getOrderField());
            } else {
                result = result.and(new Sort(Sort.Direction.fromString(dto.getOrderType()), dto.getOrderField()));
            }
        }

        if (dtos.length == 0) {
            result = basicSort();
        }
        return result;
    }
}