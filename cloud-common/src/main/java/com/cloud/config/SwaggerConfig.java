package com.cloud.config;

import com.cloud.constant.JwtConstants;
import com.cloud.swagger.PrintSwaggerRunner;
import com.cloud.swagger.SwaggerCommonConfig;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jdk.internal.util.EnvUtils;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.boot.origin.OriginTrackedValue;
import org.springframework.cloud.bootstrap.config.BootstrapPropertySource;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.*;
import org.springframework.http.MediaType;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.*;
import java.util.function.Predicate;

/**
 * swagger配置类
 *
 * @author guojianbo
 * @date 2023/6/21 17:30
 */
@Configuration
@Import(SwaggerCommonConfig.class)
public class SwaggerConfig {

    @Autowired
    private Environment environment;

    @Bean
    public Docket bDocket() {
        return createDocket("B端接口", RequestHandlerSelectors.basePackage("com.cloud.controller.admin"),
                Lists.newArrayList(
                        new RequestParameterBuilder()
                                .name(JwtConstants.DETAILS_USER_ID)
                                .description("用户id")
                                .in(ParameterType.HEADER)
                                .required(true)
                                .build()));
    }

    @Bean
    public Docket cDocket() {
        return createDocket("C端接口", RequestHandlerSelectors.basePackage("com.cloud.controller.client"),
                Lists.newArrayList(
                        new RequestParameterBuilder()
                                .name(JwtConstants.APP_USER_ID)
                                .description("app用户id")
                                .in(ParameterType.HEADER)
                                .required(true)
                                .build()));
    }

    @Bean
    public Docket feignDocket() {
        return createDocket("Feign接口", RequestHandlerSelectors.basePackage("com.cloud.feign"), null);
    }


    /**
     * @param title 标题
     * @param paths 路径
     * @Description
     * @author lgmin
     * @return: springfox.documentation.spring.web.plugins.Docket
     * @date 2021/6/17
     */
    private Docket createDocket(String title, Predicate paths, List<RequestParameter> globalRequestParameters) {
        if (globalRequestParameters == null) {
            globalRequestParameters = new ArrayList<>();
        }
        //解决springboot2.6.2与springfox3.0.0不兼容的问题
        boolean enabled = PrintSwaggerRunner.checkOpenSwagger(environment);
        return new Docket(DocumentationType.OAS_30)
                .groupName(title)
                .enable(enabled)
                .select()
                // 类需要有Api注解才能生成接口文档
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                // 方法需要有ApiOperation注解才能生成接口文档
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .apis(paths)
                .paths(PathSelectors.any())
                .build()
                .consumes(Sets.newHashSet(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .produces(Sets.newHashSet(MediaType.APPLICATION_JSON_UTF8_VALUE))
                // 接口文档的基本信息
                .apiInfo(apiInfo(title))
                .globalRequestParameters(globalRequestParameters);
    }

    @SneakyThrows
    private ApiInfo apiInfo(String title) {
        return apiInfo(title, null, null);
    }

    @SneakyThrows
    private ApiInfo apiInfo(String title, String desc, String version) {
        String serviceName = environment.getProperty("spring.application.name");

        if (StringUtils.isEmpty(desc)) {
            desc = "测试环境域名：http://xxxx/xxxx/ <br/>";
        }

        if (StringUtils.isEmpty(version)) {
            version = "1.0.0";
        }

        desc = String.format(desc, serviceName);
        return new ApiInfoBuilder()
                .title("(" + serviceName + ") " + title)
                .description(desc)
                .version(version)
                .build();
    }

}
