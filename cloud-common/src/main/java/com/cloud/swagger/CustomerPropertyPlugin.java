package com.cloud.swagger;

import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import springfox.documentation.builders.ModelSpecificationBuilder;
import springfox.documentation.builders.PropertySpecificationBuilder;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

/**
 * Date类型字段显示为long,Long和BigDecimal类型显示为string
 *
 * @author guojianbo
 * @date 2023/6/21 17:30
 */
public class CustomerPropertyPlugin implements ModelPropertyBuilderPlugin {
    @Override
    public void apply(ModelPropertyContext modelPropertyContext) {
        Optional<BeanPropertyDefinition> beanPropertyDefinition1 = modelPropertyContext.getBeanPropertyDefinition();
        if (!beanPropertyDefinition1.isPresent()) {
            return;
        }
        BeanPropertyDefinition beanPropertyDefinition = beanPropertyDefinition1.get();
        PropertySpecificationBuilder builder1 = modelPropertyContext.getSpecificationBuilder();
        if (beanPropertyDefinition.getRawPrimaryType() == Date.class) {

            builder1.type(new ModelSpecificationBuilder().scalarModel(ScalarType.LONG).build());

        } else if (beanPropertyDefinition.getRawPrimaryType() == Long.class
                || beanPropertyDefinition.getRawPrimaryType() == BigDecimal.class) {

            builder1.type(new ModelSpecificationBuilder().scalarModel(ScalarType.STRING).build());

        }
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return DocumentationType.OAS_30 == delimiter;
    }
}
