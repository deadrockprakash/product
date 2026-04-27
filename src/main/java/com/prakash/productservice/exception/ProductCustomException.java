package com.prakash.productservice.exception;

import lombok.Data;

@Data
public class ProductCustomException extends RuntimeException {
    private final String errorCode;
    public ProductCustomException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;

    }
}
