package com.gym.management.system.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

/**
 * Swagger (OpenAPI) configuration for API documentation.
 *
 * Provides:
 * - Basic API metadata (title, version, description)
 * - JWT-based authentication support in Swagger UI
 *   via the "Authorize" button
 * - Server URLs for both local and production environments
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
                        .description("API Documentation")
                        .contact(new Contact()
                                .name("Sandeep Tiwari")
                                .email("tiwarisandeep1909@gmail.com")
                                .url("https://github.com/wayneinvein")))

                // Server URLs — Swagger will use these to make API calls
                // Production server must be https
                .servers(List.of(
                        new Server()
                                .url("https://sandeeptiwari.up.railway.app")
                                .description("Production Server"),
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server")
                ))

                // Enables global security requirement so all endpoints
                // can use the configured JWT authentication
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))

                // Defines the JWT bearer authentication scheme
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}