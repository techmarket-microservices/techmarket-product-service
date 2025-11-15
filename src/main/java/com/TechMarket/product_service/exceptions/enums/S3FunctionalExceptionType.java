package com.TechMarket.product_service.exceptions.enums;

import lombok.Getter;

@Getter
public enum S3FunctionalExceptionType {
    BUCKET_NOT_FOUND("BUCKET_NOT_FOUND"),
    INVALID_FILE_EXTENSION("INVALID_FILE_EXTENSION"),
    FILE_TOO_LARGE("FILE_TOO_LARGE"),
    INVALID_FILE_TYPE("INVALID_FILE_TYPE"),
    FILE_UPLOAD_ERROR("FILE_UPLOAD_ERROR"),
    FILE_IO_ERROR("FILE_IO_ERROR"),
    MULTIPLE_EXCEPTIONS("MULTIPLE_EXCEPTIONS"),
    S3_ACCESS_ERROR("S3_ACCESS_ERROR"),
    FILE_NOT_FOUND("FILE_NOT_FOUND"),
    OBJECT_NOT_FOUND("OBJECT_NOT_FOUND");

    private final String type;

    S3FunctionalExceptionType(String type) {
        this.type = type;
    }
}
