package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.PageableTools;
import com.freelycar.saas.basic.wrapper.PaginationRJO;
import com.freelycar.saas.project.entity.Store;
import com.freelycar.saas.project.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * @author tangwei - Toby
 * @date 2018/11/28
 * @email toby911115@gmail.com
 */
@Service
public class StoreService {
    @Autowired
    StoreRepository storeRepository;


    public PaginationRJO findAll(Integer pageNumber) {
        Page<Store> storePage = storeRepository.findAll(PageableTools.basicPage(pageNumber));
        return PaginationRJO.of(storePage);
    }

}
