package com.TechMarket.product_service.exceptions;

import com.TechMarket.product_service.exceptions.enums.S3FunctionalExceptionType;
import lombok.Getter;

@Getter
public class S3FunctionalException extends Exception {
    private final S3FunctionalExceptionType code;

    public S3FunctionalException(S3FunctionalExceptionType type) {
        super(type.toString());
        this.code = type;
    }

    public S3FunctionalException(S3FunctionalExceptionType type, String message) {
        super(message);
        this.code = type;
    }
}