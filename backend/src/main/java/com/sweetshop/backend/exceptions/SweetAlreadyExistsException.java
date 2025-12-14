package com.sweetshop.backend.exceptions;

public class SweetAlreadyExistsException extends RuntimeException {
    public SweetAlreadyExistsException(String message) {
        super(message);
    }
}
