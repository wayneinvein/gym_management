package com.gym.management.system.security;

import com.gym.management.system.service.implementationclasses.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Authentication Filter
 *
 * This filter executes once per request and is responsible for:
 * 1. Extracting JWT token from Authorization header
 * 2. Validating the token
 * 3. Loading user details from DB
 * 4. Setting authentication in Spring Security context
 *
 * If token is valid → user is authenticated for the request lifecycle.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Read Authorization header from incoming request
        String authHeader = request.getHeader("Authorization");

        String token = null;
        String username = null;

        // Extract JWT token if header is in "Bearer <token>" format
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);

            // Extract username (subject) from JWT token
            username = jwtUtil.extractUsername(token);
        }

        // Proceed only if username exists and user is not already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load full user details (roles, authorities) from database
            var userDetails = userDetailsService.loadUserByUsername(username);

            //  Validate token integrity and expiry
            if (jwtUtil.validateToken(token, userDetails.getUsername())) {

                // Create authentication object for Spring Security context
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,      // authenticated principal
                                null,             // credentials not required for JWT
                                userDetails.getAuthorities() // roles/permissions
                        );

                // Set authentication in SecurityContext (marks user as logged in)
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continue request processing (controller or next filter)
        filterChain.doFilter(request, response);
    }
}