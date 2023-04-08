package org.example.exceprions;

public class RepairerNotFoundException extends RuntimeException {
    public RepairerNotFoundException(String message) {
        super(message);
    }
}
