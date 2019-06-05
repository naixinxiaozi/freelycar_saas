package com.freelycar.saas.project.service;

import com.freelycar.saas.basic.wrapper.Constants;
import com.freelycar.saas.basic.wrapper.ResultJsonObject;
import com.freelycar.saas.exception.ArgumentMissingException;
import com.freelycar.saas.exception.ObjectNotFoundException;
import com.freelycar.saas.project.entity.ClientOrderImg;
import com.freelycar.saas.project.entity.StaffOrderImg;
import com.freelycar.saas.project.entity.StoreImg;
import com.freelycar.saas.project.repository.ClientOrderImgRepository;
import com.freelycar.saas.project.repository.StaffOrderImgRepository;
import com.freelycar.saas.project.repository.StoreImgRepository;
import com.freelycar.saas.util.PhotoCompressionUtil;
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
    private static String STORE_IMG_FOLDER_NAME = "storeimg";
    private static String CLIENT_ORDER_IMG_FOLDER_NAME = "clientorderimg";
    private static String STAFF_ORDER_IMG_FOLDER_NAME = "stafforderimg";
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${upload.picture.path}")
    private String picturePath;
    @Value("${upload.picture.url}")
    private String pictureURL;
    @Autowired
    private StoreImgRepository storeImgRepository;
    @Autowired
    private ClientOrderImgRepository clientOrderImgRepository;
    @Autowired
    private StaffOrderImgRepository staffOrderImgRepository;

    /**
     * 上传门店宣传图片，返回结果中包含图片url
     *
     * @param file
     * @param request
     * @return
     * @throws FileNotFoundException
     * @throws ArgumentMissingException
     */
    public ResultJsonObject uploadStoreImg(MultipartFile file, HttpServletRequest request) throws FileNotFoundException, ArgumentMissingException {
        String resultURL = this.uploadPicture(file, request, STORE_IMG_FOLDER_NAME);


        // 文件名与文件URL存入数据库表
        if (StringUtils.hasText(resultURL)) {
            StoreImg storeImg = new StoreImg();
            storeImg.setCreateTime(new Timestamp(System.currentTimeMillis()));
            storeImg.setDelStatus(Constants.DelStatus.NORMAL.isValue());
            storeImg.setUrl(resultURL);

            StoreImg result = storeImgRepository.save(storeImg);
            return ResultJsonObject.getDefaultResult(result);
        }
        return ResultJsonObject.getErrorResult(null, "上传失败");
    }

    /**
     * 用户上传智能柜订单图片，返回结果中包含图片url
     *
     * @param file
     * @param request
     * @return
     * @throws FileNotFoundException
     * @throws ArgumentMissingException
     */
    public ResultJsonObject uploadClientOrderImg(MultipartFile file, HttpServletRequest request) throws FileNotFoundException, ArgumentMissingException {
        String resultURL = this.uploadPicture(file, request, CLIENT_ORDER_IMG_FOLDER_NAME);

        if (StringUtils.hasText(resultURL)) {
            ClientOrderImg clientOrderImg = new ClientOrderImg();
            clientOrderImg.setCreateTime(new Timestamp(System.currentTimeMillis()));
            clientOrderImg.setDelStatus(Constants.DelStatus.NORMAL.isValue());
            clientOrderImg.setUrl(resultURL);

            ClientOrderImg result = clientOrderImgRepository.save(clientOrderImg);
            return ResultJsonObject.getDefaultResult(result);
        }
        return ResultJsonObject.getErrorResult(null, "上传失败");
    }

    /**
     * 技师上传智能柜订单图片，返回结果中包含图片url
     *
     * @param file
     * @param request
     * @return
     * @throws FileNotFoundException
     * @throws ArgumentMissingException
     */
    public ResultJsonObject uploadStaffOrderImg(MultipartFile file, HttpServletRequest request) throws FileNotFoundException, ArgumentMissingException {
        String resultURL = this.uploadPicture(file, request, STAFF_ORDER_IMG_FOLDER_NAME);

        if (StringUtils.hasText(resultURL)) {
            StaffOrderImg staffOrderImg = new StaffOrderImg();
            staffOrderImg.setCreateTime(new Timestamp(System.currentTimeMillis()));
            staffOrderImg.setDelStatus(Constants.DelStatus.NORMAL.isValue());
            staffOrderImg.setUrl(resultURL);

            StaffOrderImg result = staffOrderImgRepository.save(staffOrderImg);
            return ResultJsonObject.getDefaultResult(result);
        }
        return ResultJsonObject.getErrorResult(null, "上传失败");
    }

    /**
     * 上传图片到服务器上，生成图片url
     *
     * @param file
     * @param request
     * @param folderName 文件夹名称（）
     * @return
     * @throws FileNotFoundException
     * @throws ArgumentMissingException
     */
    public String uploadPicture(MultipartFile file, HttpServletRequest request, String folderName) throws FileNotFoundException, ArgumentMissingException {
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

        //压缩后的图片文件名
        String litePath = df.format(calendar.getTime()) + uuid + "_lite.jpg";

        logger.info(path);

        //保存文件的绝对路径
        String filePath = picturePath + File.separator + folderName + File.separator + path;
        String liteFilePath = picturePath + File.separator + folderName + File.separator + litePath;
        logger.info("绝对路径:" + filePath);

        File newFile = new File(filePath);

        // 检测是否存在目录
        if (!newFile.getParentFile().exists()) {
            // 新建文件夹
            newFile.getParentFile().mkdirs();
        }

        String url = null;

        //MultipartFile的方法直接写文件
        try {
            //上传文件
            file.transferTo(newFile);

            //执行图片压缩
            String targetResultPath = PhotoCompressionUtil.compress(filePath, liteFilePath);
            if (null == targetResultPath) {
                return null;
            }

            //数据库存储的相对路径
            url = pictureURL + "/" + folderName + "/" + litePath;
            logger.info("相对路径:" + url);

        } catch (IllegalStateException | IOException | ObjectNotFoundException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return url;
    }

    public ResultJsonObject importProjects(String storeId, MultipartFile file, HttpServletRequest request) {
        // TODO 上传Excel导入门店项目

        //判断storeId和file文件是否为空

        //根据storeId查询出有效的项目类型

        //解析excel文件

        //遍历


        return null;
    }
}
