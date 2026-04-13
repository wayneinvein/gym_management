package com.gym.management.system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO for sending error details in API responses.
 * Standardizes error structure across the application.
 */
@Data
@AllArgsConstructor
public class ErrorResponseDTO {

    // Timestamp when the error occurred
    private LocalDateTime timestamp;

    // HTTP status code (e.g., 400, 404, 500)
    private int status;

    // Error message describing what went wrong
    private String message;
}