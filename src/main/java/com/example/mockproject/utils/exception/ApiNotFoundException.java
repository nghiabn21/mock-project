package com.example.mockproject.utils.exception;

public class ApiNotFoundException extends RuntimeException{
    public ApiNotFoundException(String message) {
        super(message);
    }
}
