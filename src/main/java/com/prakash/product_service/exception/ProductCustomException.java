package com.prakash.product_service.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProductCustomException extends RuntimeException {
    private final String errorCode;
    public ProductCustomException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;

    }
}
