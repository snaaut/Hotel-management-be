package com.datn.hotelmanagement.exception;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
@Getter
public enum ErrorCode {
    INVALID_KEY("Uncategorized error", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD("Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED("Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("You do not have permission", HttpStatus.FORBIDDEN),
    UNCATEGORIZED_EXCEPTION("Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    USERNAME_INVALID("Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_EXISTED("User existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED("User not existed", HttpStatus.NOT_FOUND),
    ;

    ErrorCode(String message, HttpStatusCode statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    private final String message;
    private final HttpStatusCode statusCode;
}