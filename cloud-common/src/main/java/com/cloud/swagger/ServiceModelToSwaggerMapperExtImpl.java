package com.cloud.swagger;

import io.swagger.models.Operation;
import io.swagger.models.Path;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ApiListing;
import springfox.documentation.service.ModelNamesRegistry;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2MapperImpl;

import java.util.*;

import static java.util.Optional.ofNullable;
import static springfox.documentation.builders.BuilderDefaults.nullToEmptyList;

/**
 * 按ApiOperation注解的value值进行排序
 *
 * @author guojianbo
 * @date 2023/6/21 17:30
 */
@SuppressWarnings("all")
public class ServiceModelToSwaggerMapperExtImpl extends ServiceModelToSwagger2MapperImpl {
    @Override
    protected Map<String, Path> mapApiListings(Map<String, List<ApiListing>> apiListings) {
        Map<String, Path> paths = new LinkedHashMap<>();
        apiListings.values().stream()
                .flatMap(Collection::stream)
                .forEachOrdered(each -> {
                    List<ApiDescription> apis = each.getApis();
                    apis.sort((a1, a2) -> {
                        return a1.getOperations().get(0).getSummary().compareTo(a2.getOperations().get(0).getSummary());
                    });
                    for (ApiDescription api : apis) {
                        paths.put(
                                api.getPath(),
                                mapOperations(api, ofNullable(paths.get(api.getPath())), each.getModelNamesRegistry())
                        );
                    }
                });
        return paths;
    }

    private Path mapOperations(
            ApiDescription api,
            Optional<Path> existingPath,
            ModelNamesRegistry modelNamesRegistry) {
        Path path = existingPath.orElse(new Path());
        for (springfox.documentation.service.Operation each : nullToEmptyList(api.getOperations())) {
            Operation operation = mapOperation(each, modelNamesRegistry);
            path.set(each.getMethod().toString().toLowerCase(), operation);
        }
        return path;
    }

}
