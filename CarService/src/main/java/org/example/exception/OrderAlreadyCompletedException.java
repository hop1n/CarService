package org.example.exception;

public class OrderAlreadyCompletedException extends RuntimeException {
    public OrderAlreadyCompletedException(String message){
        super(message);
    }
}
