package com.gym.management.system.exception;

public class MembershipAlreadyPresentException extends RuntimeException{
    public MembershipAlreadyPresentException(String message) {
        super(message);
    }
}
