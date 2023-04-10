package org.example.exception;

public class RepairerNotAvailableException extends RuntimeException{
    public RepairerNotAvailableException(String message){
        super(message);
    }
}
