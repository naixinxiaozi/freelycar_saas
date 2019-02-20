package com.freelycar.saas.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.function.Predicate;

/**
 * @author tangwei - Toby
 * @date 2019-01-28
 * @email toby911115@gmail.com
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${swagger.enable}")
    private boolean enableSwagger;

    @Bean
    public Docket createRestApi() {
        Predicate<RequestHandler> predicate = input -> {
            Class<?> declaringClass = input.declaringClass();
            if (declaringClass == BasicErrorController.class) {// 排除

                return false;
            }
            if (declaringClass.isAnnotationPresent(RestController.class)) { // 被注解的类

                return true;
            }
            // 被注解的方法
            return input.isAnnotatedWith(ResponseBody.class);

        };
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .enable(enableSwagger)
                .useDefaultResponseMessages(false)
                .select()
                .apis(predicate::test)
                .apis(RequestHandlerSelectors.basePackage("com.freelycar.saas")) //过滤的接口
                .build();
    }

    private ApiInfo getApiInfo() {
        // 定义联系人信息
        Contact contact = new Contact("tangwei", "https://github.com/tangtang233", "toby911115@163.com");
        return new ApiInfoBuilder()
                .title("小易爱车SaaS平台接口文档-Swagger2生成") // 标题
                .description("小易爱车SaaS平台接口文档，包含门店端、管理端、微信端") // 描述信息
                .version("1.0.0") // //版本
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0")
                .contact(contact)
                .build();
    }
}
