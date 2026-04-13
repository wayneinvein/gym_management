package com.gym.management.system.exception;

import com.gym.management.system.dto.response.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application.
 * Centralizes all exception handling and returns consistent API error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handles all unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(Exception ex) {

        ErrorResponseDTO response = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Something went wrong"
        );

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handles validation errors from @Valid annotations
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage())
                );

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // Handles resource not found exceptions
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFoundException(NotFoundException ex) {

        ErrorResponseDTO response = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Handles invalid input exceptions
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidInputException(InvalidInputException ex) {

        ErrorResponseDTO response = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handles duplicate or already existing resource exceptions
    @ExceptionHandler(AlreadyPresentException.class)
    public ResponseEntity<ErrorResponseDTO> handleAlreadyPresentException(AlreadyPresentException ex) {

        ErrorResponseDTO response = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                ex.getMessage()
        );

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}