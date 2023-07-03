package com.cloud.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author guojianbo
 * @date 2023/6/21 17:30
 */
@Configuration
public class SwaggerCommonConfig {

    @Bean
    public CustomerPropertyPlugin customerSwaggerProperty() {
        return new CustomerPropertyPlugin();
    }

    @Bean
    public PrintSwaggerRunner printSwaggerRunner() {
        return new PrintSwaggerRunner();
    }

    @Bean
    @Primary
    public ServiceModelToSwaggerMapperExtImpl serviceModelToSwaggerMapperExt() {
        return new ServiceModelToSwaggerMapperExtImpl();
    }

}
