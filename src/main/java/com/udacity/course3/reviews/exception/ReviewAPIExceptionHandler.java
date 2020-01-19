package com.udacity.course3.reviews.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice(annotations = RestController.class)
public class ReviewAPIExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException e){
        ReviewAPIError reviewAPIError = new ReviewAPIError(HttpStatus.NOT_FOUND,e.getMessage(), e);
        return buildResponseEntity(reviewAPIError);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<Object> handleException(Exception e){
        ReviewAPIError reviewAPIError = new ReviewAPIError(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage(), e);
        return buildResponseEntity(reviewAPIError);
    }

    private ResponseEntity<Object> buildResponseEntity(ReviewAPIError reviewAPIError){
        return new ResponseEntity<>(reviewAPIError,reviewAPIError.getHttpStatus());
    }
}
