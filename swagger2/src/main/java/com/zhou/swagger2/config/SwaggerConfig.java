package com.zhou.swagger2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author: zhou.liu
 * @Date: 2022/4/25 16:49
 * @Description:
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    /**
     * 创建一个Docket对象 调用select()方法， 生成ApiSelectorBuilder对象实例，该对象负责定义外漏的API入口
     * 通过使用RequestHandlerSelectors和PathSelectors来提供Predicate，在此我们使用any()方法，将所有API都通过Swagger进行文档管理
     *
     * @return
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(true)
                .select()
                //如果不想将所有的接口都通过swagger管理的话，可以将RequestHandlerSelectors.any()修改为RequestHandlerSelectors.basePackage()
                //.apis(RequestHandlerSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.zhou.swagger2"))
                .paths(PathSelectors.ant("/**"))
                .build();
    }

    @SuppressWarnings("deprecation")
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 标题
                .title("SpringBoot2 中使用Swagger2 构建RESTful APIs")
                // 简介
                .description("This a demo for Swagger2")
                // 服务条款
                .termsOfServiceUrl("http://127.0.0.1:9090/v2/api-docs")
                // 作者个人信息
                .contact("zhou.liu")
                // 版本
                .version("1.0").build();
    }
}
