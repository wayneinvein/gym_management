package com.gym.management.system.exception;

public class MembershipNotFoundException extends RuntimeException{
    public MembershipNotFoundException(String message) {
        super(message);
    }
}
