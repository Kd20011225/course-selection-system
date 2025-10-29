package com.example.demo.businessExceptions;

public class UserException extends RuntimeException {
    public UserException(String message) {
        super(message);
    }
}
