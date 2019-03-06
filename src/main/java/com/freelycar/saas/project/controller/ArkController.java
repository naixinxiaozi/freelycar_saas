package com.freelycar.saas.project.controller;

import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.exception.ArgumentMissingException;
import com.freelycar.saas.exception.DataIsExistException;
import com.freelycar.saas.project.entity.Ark;
import com.freelycar.saas.project.service.ArkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tangwei - Toby
 * @date 2019-02-12
 * @email toby911115@gmail.com
 */
@RestController
@RequestMapping("/ark")
public class ArkController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ArkService arkService;

    /**
     * 新增智能柜（含生成状态表数据）
     *
     * @param ark
     * @return
     */
    @PostMapping("/add")
    public ResultJsonObject addArk(@RequestBody Ark ark) {
        try {
            return ResultJsonObject.getDefaultResult(arkService.addArk(ark));
        } catch (ArgumentMissingException | DataIsExistException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return ResultJsonObject.getErrorResult(null, e.getMessage());
        }
    }
}
