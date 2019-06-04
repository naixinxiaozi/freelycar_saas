package com.freelycar.saas.util;

import com.freelycar.saas.exception.ObjectNotFoundException;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * @author tangwei - Toby
 * @date 2019-06-04
 * @email toby911115@gmail.com
 */
public class PhotoCompressionUtil {

    private static Logger logger = LoggerFactory.getLogger(PhotoCompressionUtil.class);

    public static String compress(String sourcePath, String targetPath) throws ObjectNotFoundException {
        if (StringUtils.isEmpty(sourcePath)) {
            throw new ObjectNotFoundException("参数sourcePath为空值");
        }
        if (StringUtils.isEmpty(targetPath)) {
            throw new ObjectNotFoundException("参数targetPath为空值");
        }
        try {
            Thumbnails.of(sourcePath)
                    .scale(1f)
                    .outputQuality(0.5f)
                    .toFile(targetPath);
        } catch (IOException e) {
            logger.error("图片压缩时发生IO异常");
            e.printStackTrace();
            return null;
        }
        return targetPath;
    }
}
