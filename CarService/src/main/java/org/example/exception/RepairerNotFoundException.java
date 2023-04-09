package org.example.exception;

public class RepairerNotFoundException extends RuntimeException {
    public RepairerNotFoundException(String message) {
        super(message);
    }
}
