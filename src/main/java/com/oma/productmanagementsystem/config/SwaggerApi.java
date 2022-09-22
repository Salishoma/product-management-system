package com.oma.productmanagementsystem.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.util.Collections;

import static com.google.common.collect.Lists.newArrayList;

@Configuration
public class SwaggerApi {

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .pathsToMatch("**")
                .group("salishoma")
                .build();
    }

}

