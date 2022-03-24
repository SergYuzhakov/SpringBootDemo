package com.sbapp.todo.util.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;

public class SqlUniqueConstraintException extends AppException{
    public SqlUniqueConstraintException(String message) {
        super(HttpStatus.FORBIDDEN, message, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE));
    }
}
