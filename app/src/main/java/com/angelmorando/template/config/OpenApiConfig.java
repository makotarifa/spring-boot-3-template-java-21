package com.angelmorando.template.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(title = "Template API", version = "v1", description = "Template project API documentation")
)
public class OpenApiConfig {

}
