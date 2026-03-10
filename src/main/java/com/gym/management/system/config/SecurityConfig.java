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
                        .requestMatchers(HttpMethod.POST, "/users/register").permitAll()

                        .requestMatchers(HttpMethod.GET,"/users/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT,"/users/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE,"/users/**")
                        .hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .userDetailsService(customUserDetailsService)

                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}