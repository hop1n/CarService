package org.example.exception;

public class GarageNotAvailableException extends RuntimeException{
    public GarageNotAvailableException(String message){
        super(message);
    }
}
