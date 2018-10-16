package com.freelycar.saas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

/**
 * @author toby
 */
@SpringBootApplication
public class BootApplication {

    private static Logger logger = LoggerFactory.getLogger(BootApplication.class);

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(BootApplication.class, args);

        logger.info("通过SpringBoot来注入依赖:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            logger.info(beanName);
        }
        logger.info("mps服务启动完成。");
    }
}
