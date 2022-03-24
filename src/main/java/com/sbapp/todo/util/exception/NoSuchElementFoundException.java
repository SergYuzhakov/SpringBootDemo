package com.sbapp.todo.util.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;


public class NoSuchElementFoundException extends AppException {
    public NoSuchElementFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE));
    }
}
