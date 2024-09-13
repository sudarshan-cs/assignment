package com.example.assignment.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenAPIConfiguration {
    private static final String BEARER_TOKEN = "Bearer";
    public static final String AUTHORISATION_TOKEN = "Authorization";

    @Value("${svc.service.swagger.title}")
    String title;

    @Value("${svc.service.swagger.description}")
    String description;



    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new io.swagger.v3.oas.models.info.Info()
                        .title(title)
                        .description(description))
                .components(new Components()
                        .addSecuritySchemes(AUTHORISATION_TOKEN, bearerTokenSecurityScheme()))
                .security(Collections.singletonList(new SecurityRequirement().addList(AUTHORISATION_TOKEN)));
    }

    public SecurityScheme bearerTokenSecurityScheme() {
        return new SecurityScheme()
                .scheme(BEARER_TOKEN)
                .name(AUTHORISATION_TOKEN) // authorisation-token
                .description("Authorization Header Required only for the Private APIs")
                .in(SecurityScheme.In.HEADER)
                .type(SecurityScheme.Type.HTTP);
    }


}
