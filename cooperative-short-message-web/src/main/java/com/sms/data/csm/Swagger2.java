package com.sms.data.csm;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 接口文档生成类
 *
 * @author leijin
 * @create 2017-07-17 14:09
 **/
@Configuration
@EnableSwagger2
public class Swagger2 {
    @Bean
    @Profile({"test", "dev"})
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sms.data.platform.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    @Profile({"prod"})
    public Docket createRestApiProd() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sms.data.platform.controller"))
                .paths(PathSelectors.none())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("合作短信平台接口文档")
                .description("1.0版本接口文档")
                .termsOfServiceUrl("http://www.baidu.com/")
                .contact("sms")
                .version("1.0")
                .build();
    }
}

