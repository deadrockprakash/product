package com.prakash.product_service.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class RestResponseEntityHandler {
    @ExceptionHandler(ProductCustomException.class)
    public ResponseEntity<ErrorMessage> handleProductServiceException(
            ProductCustomException exception,
            HttpServletRequest request
    ) {
        return new ResponseEntity<>(
                buildError(exception.getMessage(), exception.getErrorCode(), HttpStatus.NOT_FOUND, request, null),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleUsernameNotFoundException(
            UsernameNotFoundException exception,
            HttpServletRequest request
    ) {
        return new ResponseEntity<>(
                buildError(exception.getMessage(), "USER_NOT_FOUND", HttpStatus.NOT_FOUND, request, null),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        Map<String, String> validationErrors = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                validationErrors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(
                buildError("Request validation failed", "VALIDATION_FAILED", HttpStatus.BAD_REQUEST, request, validationErrors),
                HttpStatus.BAD_REQUEST
        );
    }

    private ErrorMessage buildError(
            String message,
            String errorCode,
            HttpStatus status,
            HttpServletRequest request,
            Map<String, String> errors
    ) {
        return ErrorMessage.builder()
                .message(message)
                .errorCode(errorCode)
                .status(status.value())
                .path(request.getRequestURI())
                .timestamp(Instant.now())
                .errors(errors)
                .build();
    }

}
