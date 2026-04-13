package com.gym.management.system.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger (OpenAPI) configuration for API documentation.
 *
 * Provides:
 * - Basic API metadata (title, version, description)
 * - JWT-based authentication support in Swagger UI
 *   via the "Authorize" button
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // API metadata displayed in Swagger UI
                .info(new Info()
                        .title("Gym Management System APIs")
                        .version("1.0")
                        .description("API Documentation BY Sandeep Tiwari"))

                // Enables global security requirement so all endpoints
                // can use the configured JWT authentication
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))

                // Defines the JWT bearer authentication scheme
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .name("Authorization") // HTTP header name
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")      // Must be "bearer"
                                        .bearerFormat("JWT"))); // Token format (for UI hint)
    }
}