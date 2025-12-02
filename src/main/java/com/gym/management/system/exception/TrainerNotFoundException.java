package com.gym.management.system.exception;

public class TrainerNotFoundException extends RuntimeException {
    public TrainerNotFoundException(String message){
        super(message);
    }
}
