package com.udacity.course3.reviews.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String exceptionMessage){
        super(exceptionMessage);
    }
}
