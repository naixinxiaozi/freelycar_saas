package com.freelycar.saas.project.controller;

import com.freelycar.saas.aop.LoggerManage;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.exception.ArgumentMissingException;
import com.freelycar.saas.project.service.UploadService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;

/**
 * @author tangwei - Toby
 * @date 2019-03-12
 * @email toby911115@gmail.com
 */
@RestController
@RequestMapping("/upload")
public class UploadController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UploadService uploadService;

    @ApiOperation(value = "上传门店宣传图片", produces = "application/json")
    @LoggerManage(description = "调用方法：上传门店宣传图片")
    @PostMapping("/storeimg")
    public ResultJsonObject uploadStoreImg(MultipartFile file, HttpServletRequest request) {
        try {
            return uploadService.uploadPicture(file, request);
        } catch (FileNotFoundException | ArgumentMissingException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return ResultJsonObject.getErrorResult(null, e.getMessage());
        }
    }

    @ApiOperation(value = "上传Excel-门店项目导入", produces = "application/json")
    @LoggerManage(description = "调用方法-上传Excel-门店项目导入")
    @PostMapping("/importProjects")
    public ResultJsonObject importProjects(String storeId, MultipartFile file, HttpServletRequest request) {
        return uploadService.importProjects(storeId, file, request);
    }
}
