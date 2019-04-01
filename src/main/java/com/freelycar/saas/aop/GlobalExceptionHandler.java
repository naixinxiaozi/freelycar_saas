package com.freelycar.saas.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author tangwei - Toby
 * @date 2019-03-13
 * @email toby911115@gmail.com
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

//    @ExceptionHandler(Exception.class)
//    public String handleException(Exception e, RedirectAttributes redirectAttributes) {
//        logger.error("捕获到通用的异常：");
//        logger.error(e.getMessage(), e);
//        redirectAttributes.addFlashAttribute("message", e.getCause().getMessage());
//        return "捕获到通用的异常";
//    }

    @ExceptionHandler(MultipartException.class)
    public String handleMultipartError(MultipartException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", e.getCause().getMessage());
        return "捕获到上传文件异常";
    }
}
