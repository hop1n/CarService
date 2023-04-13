package org.example.exception;

public class JsonParsingException extends RuntimeException{
    public JsonParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
