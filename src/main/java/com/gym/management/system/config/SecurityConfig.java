package com.gym.management.system.config;

import com.gym.management.system.service.implememtationclasses.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // First admin registration
                        .requestMatchers(HttpMethod.POST, "/users/register").permitAll()

                        // USER MANAGEMENT
                        .requestMatchers("/users/**")
                        .hasRole("ADMIN")

                        // TRAINER ENDPOINTS
                        .requestMatchers(HttpMethod.GET, "/api/trainers/**")
                        .hasAnyRole("TRAINER","ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/trainers/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/api/trainers/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/api/trainers/**")
                        .hasRole("ADMIN")

                        // MEMBER ENDPOINTS
                        .requestMatchers(HttpMethod.GET, "/api/members/**")
                        .hasAnyRole("MEMBER","ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/members/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/api/members/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/api/members/**")
                        .hasRole("ADMIN")

                        // MEMBERSHIP PLAN ENDPOINTS
                        .requestMatchers(HttpMethod.GET, "/plans/**")
                        .hasAnyRole("ADMIN","TRAINER","MEMBER")

                        .requestMatchers(HttpMethod.POST, "/plans/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/plans/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/plans/**")
                        .hasRole("ADMIN")

                        // MEMBERSHIP ENDPOINTS
                        .requestMatchers(HttpMethod.GET, "/api/memberships/**")
                        .hasAnyRole("ADMIN","MEMBER")

                        .requestMatchers(HttpMethod.POST, "/api/memberships/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/api/memberships/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/api/memberships/**")
                        .hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .userDetailsService(customUserDetailsService)

                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}