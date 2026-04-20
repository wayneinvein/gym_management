package com.gym.management.system.config;

import com.gym.management.system.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Central security configuration for the application.
 *
 * Configures:
 * - JWT-based authentication (stateless)
 * - Endpoint authorization rules
 * - Security filters
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Enables method-level security annotations like @PreAuthorize
@RequiredArgsConstructor
public class SecurityConfig {

    // Custom JWT filter to validate tokens on each request
    private final JwtAuthenticationFilter jwtFilter;

    /**
     * Defines the security filter chain.
     *
     * Key decisions:
     * - Disable CSRF (not needed for stateless APIs)
     * - Use stateless session (JWT-based authentication)
     * - Allow public access to auth + Swagger endpoints
     * - Secure all other endpoints
     * - Register JWT filter before Spring's default auth filter
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Disable CSRF as we are not using session-based authentication
                .csrf(csrf -> csrf.disable())

                // Ensure Spring Security does not create or use HTTP sessions
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Define authorization rules for endpoints
                .authorizeHttpRequests(auth -> auth
                        // Allow API documentation endpoints without authentication
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // Allow authentication-related endpoints (login, register, etc.)
                        .requestMatchers("/auth/**").permitAll()

                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied"))
                )

                // Add JWT filter before the default UsernamePasswordAuthenticationFilter
                // so that token validation happens early in the filter chain
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Exposes AuthenticationManager bean.
     *
     * Required for manual authentication (e.g., during login),
     * where credentials are validated against UserDetailsService.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Password encoder bean.
     *
     * BCrypt is used as it is a strong hashing algorithm with built-in salting.
     * Recommended for storing user passwords securely.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}