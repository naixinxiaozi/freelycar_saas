package com.freelycar.saas.aop;

import java.lang.annotation.*;

/**
 * @Description: 日志注解
 * @author Leo Wu
 * @date 2016年7月7日  上午11:34:57
 * @version 1.0
 */
@Target(ElementType.METHOD)  
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoggerManage {

	public String description();
}
