package com.gym.management.system.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Utility class for accessing security-related information.
 *
 * Currently used to fetch details of the authenticated user
 * from Spring Security Context.
 */
@Component
public class SecurityUtils {

    /**
     * Returns the username of the currently authenticated user.
     *
     * Reads from Spring SecurityContext which is populated
     * by JwtAuthenticationFilter after successful authentication.
     *
     * @return username if user is authenticated, otherwise null
     */
    public String getCurrentUsername() {

        // Check if authentication object exists in security context
        if (SecurityContextHolder.getContext().getAuthentication() != null) {

            // Return the logged-in user's username
            return SecurityContextHolder.getContext().getAuthentication().getName();
        }

        // No authenticated user found
        return null;
    }
}