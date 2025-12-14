package com.sweetshop.backend.exceptions;

public class InvalidSweetException extends RuntimeException {
    public InvalidSweetException(String message) {
        super(message);
    }
}
