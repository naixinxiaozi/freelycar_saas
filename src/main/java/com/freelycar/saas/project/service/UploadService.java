package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.Constants;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.exception.ArgumentMissingException;
import com.freelycar.saas.project.entity.StoreImg;
import com.freelycar.saas.project.repository.StoreImgRepository;
import com.freelycar.saas.util.UUIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author tangwei - Toby
 * @date 2019-03-12
 * @email toby911115@gmail.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UploadService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${upload.picture.path}")
    private String picturePath;

    @Value("${upload.picture.url}")
    private String pictureURL;

    @Autowired
    private StoreImgRepository storeImgRepository;

    public ResultJsonObject uploadPicture(MultipartFile file, HttpServletRequest request) throws FileNotFoundException, ArgumentMissingException {
        if (null == file || file.isEmpty()) {
            throw new FileNotFoundException("提交的图片流未获取到");
        }

        // 获取文件名
        String fileName = file.getOriginalFilename();
        logger.info("上传的文件名为：" + fileName);
        // 获取文件的后缀名
        if (StringUtils.isEmpty(fileName)) {
            throw new ArgumentMissingException("fileName为空值，上传过程异常。");
        }
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        logger.info("文件的后缀名为：" + suffixName);

        //保存时的文件名
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar calendar = Calendar.getInstance();
        String uuid = new UUIDGenerator().generate();
        String path = df.format(calendar.getTime()) + uuid + suffixName;

        logger.info(path);

        //保存文件的绝对路径
        String filePath = picturePath + File.separator + path;
        logger.info("绝对路径:" + filePath);

        File newFile = new File(filePath);

        // 检测是否存在目录
        if (!newFile.getParentFile().exists()) {
            newFile.getParentFile().mkdirs();// 新建文件夹
        }

        //MultipartFile的方法直接写文件
        try {
            //上传文件
            file.transferTo(newFile);

            //数据库存储的相对路径
            String url = pictureURL + path;
            logger.info("相对路径:" + url);

            // 文件名与文件URL存入数据库表
            StoreImg storeImg = new StoreImg();
            storeImg.setCreateTime(new Timestamp(System.currentTimeMillis()));
            storeImg.setDelStatus(Constants.DelStatus.NORMAL.isValue());
            storeImg.setUrl(url);

            StoreImg result = storeImgRepository.save(storeImg);

            return ResultJsonObject.getDefaultResult(result);
        } catch (IllegalStateException | IOException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return ResultJsonObject.getErrorResult(null, "上传失败");
    }
}
