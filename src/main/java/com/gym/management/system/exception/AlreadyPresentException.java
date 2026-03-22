package com.gym.management.system.exception;

public class AlreadyPresentException extends RuntimeException{
    public AlreadyPresentException(String message) {
        super(message);
    }
}
