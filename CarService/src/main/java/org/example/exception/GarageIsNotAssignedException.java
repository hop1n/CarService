package org.example.exception;

public class GarageIsNotAssignedException extends RuntimeException {
    public GarageIsNotAssignedException(String message){
        super(message);
    }
}
