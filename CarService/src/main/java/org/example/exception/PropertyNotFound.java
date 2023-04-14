package org.example.exception;

public class PropertyNotFound extends RuntimeException{
    public PropertyNotFound(String message) {
        super(message);
    }
}
