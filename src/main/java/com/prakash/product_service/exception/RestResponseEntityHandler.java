package com.prakash.product_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestResponseEntityHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ProductCustomException.class)
    public ResponseEntity<ErrorMessage> handleProductServiceException(ProductCustomException exception) {
        new ErrorMessage();
        return new  ResponseEntity<>( ErrorMessage.builder()
                .errorCode(exception.getErrorCode())
                .message(exception.getMessage())
                .build(), HttpStatus.NOT_FOUND);
    }

}
