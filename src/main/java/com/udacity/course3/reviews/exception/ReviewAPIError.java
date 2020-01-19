package com.udacity.course3.reviews.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ReviewAPIError {

    private HttpStatus httpStatus;

    private String message;

    private LocalDateTime timeStamp;

    private String debugMessage;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    ReviewAPIError(HttpStatus httpStatus, String message, Throwable ex){
        this.httpStatus = httpStatus;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
        timeStamp = LocalDateTime.now();
    }
}
