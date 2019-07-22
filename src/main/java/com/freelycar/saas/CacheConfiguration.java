package com.freelycar.saas;

import com.freelycar.saas.util.CacheKeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author toby
 */
@Configuration
@EnableCaching
public class CacheConfiguration extends CachingConfigurerSupport {

    private static Logger logger = LoggerFactory.getLogger(CacheConfiguration.class);

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        logger.info("------------------使用自定义key generator ---------------");
        return new CacheKeyGenerator();
    }

}
