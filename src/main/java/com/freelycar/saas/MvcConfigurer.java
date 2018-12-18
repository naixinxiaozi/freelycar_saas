package com.freelycar.saas;

import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author tangwei
 */

/*@Configuration
@EnableWebMvc
@EnableTransactionManagement
@EnableAutoConfiguration
@ComponentScan(basePackages = {
        "com.freelycar.saas.**.controller", "com.freelycar.saas.**.repository"
})*/
public class MvcConfigurer implements WebMvcConfigurer {
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        /*
         * 1.ServletMappings 设置的是 "/" 2.setUseSuffixPatternMatch默认设置为true,
         * 那么,"/user" 就会匹配 "/user.*",也就是说,"/user.html" 的请求会被 "/user" 的 Controller所拦截.
         * 3.如果该值为false,则不匹配
         */
        configurer.setUseSuffixPatternMatch(false);

        /*
         * setUseTrailingSlashMatch的默认值为true
         * 也就是说, "/user" 和 "/user/" 都会匹配到 "/user"的Controller
         */
        configurer.setUseTrailingSlashMatch(true);
    }


}