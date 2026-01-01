package com.gym.management.system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //handling member not found
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleMemberNotFoundException(MemberNotFoundException ex){
        Map<String, String> response = new HashMap<>();
        response.put("Status: 404 NOT FOUND", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    //handling trainer not found
    @ExceptionHandler(TrainerNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleTrainerNotFoundException(TrainerNotFoundException ex){
        Map<String, String> response = new HashMap<>();
        response.put("Status: 404 NOT FOUND", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    //handling membership not found exception
    @ExceptionHandler(MembershipNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleMembershipNotFoundException(MembershipNotFoundException ex){
        Map<String, String> response = new HashMap<>();
        response.put("Status: 404 NOT FOUND", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    //handling membership already present exception
    @ExceptionHandler(MembershipAlreadyPresentException.class)
    public ResponseEntity<Map<String, String>> MembershipAlreadyPresentException(MembershipAlreadyPresentException ex){
        Map<String, String> response = new HashMap<>();
        response.put("Status: 409 Conflict, Already Present", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    //handling invalid input exception
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<Map<String, String>> InvalidInputException(InvalidInputException ex){
        Map<String, String> response = new HashMap<>();
        response.put("Status: NOT ACCEPTABLE", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }

    //handling plan not exist exception
    @ExceptionHandler(PlanDoNotExistException.class)
    public ResponseEntity<Map<String, String>> PlanDoNotExistException(PlanDoNotExistException ex){
        Map<String, String> response = new HashMap<>();
        response.put("Status: NOT FOUND", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
