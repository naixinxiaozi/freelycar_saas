package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.Constants;
import com.freelycar.saas.basic.wrapper.PageableTools;
import com.freelycar.saas.basic.wrapper.PaginationRJO;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.project.entity.Store;
import com.freelycar.saas.project.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author tangwei - Toby
 * @date 2018/11/28
 * @email toby911115@gmail.com
 */
@Service
public class StoreService {
    @Autowired
    private StoreRepository storeRepository;


    public PaginationRJO findAll(Integer pageNumber) {
        Page<Store> storePage = storeRepository.findAll(PageableTools.basicPage(pageNumber));
        return PaginationRJO.of(storePage);
    }

    /**
     * 查询所有门店信息（包含模糊查询）
     *
     * @param name
     * @return
     */
    public List<Store> findAllByName(String name) {
        return storeRepository.findStoreByDelStatusAndNameContainingOrderByCreateTimeAsc(Constants.DelStatus.NORMAL.isValue(), name);
    }


    /**
     * 通过ID查询门店信息
     *
     * @param id
     * @return
     */
    public ResultJsonObject getDetail(String id) {
        Optional<Store> optionalStore = storeRepository.findById(id);
        if (!optionalStore.isPresent()) {
            return ResultJsonObject.getErrorResult(id, "查询失败，为找到id为：" + id + "的门店信息");
        }
        return ResultJsonObject.getDefaultResult(optionalStore.get());
    }

    public ResultJsonObject listAllStoreLocation() {
        return ResultJsonObject.getDefaultResult(this.findAllEffectiveStores());
    }

    /**
     * 查询所以有效的门店
     *
     * @return
     */
    public List<Store> findAllEffectiveStores() {
        return storeRepository.findAllByDelStatus(Constants.DelStatus.NORMAL.isValue());
    }

}
