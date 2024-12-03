package com.datn.hotelmanagement.exception;

import com.datn.hotelmanagement.common.domain.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException exception) {
        return new ResponseEntity<>(ApiResponse.error(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
