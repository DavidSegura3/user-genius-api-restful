package com.nisum.api.cl.infrastructure.adapters.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "API RESTful user creation",
        version = "1.0",
        description = "Documentation for endpoints for the user API ")
)
public class OpenApiConfig {
}
