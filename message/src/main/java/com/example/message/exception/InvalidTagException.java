package com.example.message.exception;

public class InvalidTagException extends RuntimeException {
    public InvalidTagException(String message) {
        super(message);
    }
}
