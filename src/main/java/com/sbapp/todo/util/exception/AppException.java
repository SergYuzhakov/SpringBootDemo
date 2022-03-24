package com.sbapp.todo.util.exception;

import lombok.Getter;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class AppException extends ResponseStatusException {
    private final ErrorAttributeOptions attributeOptions;

    public AppException(HttpStatus status, String message, ErrorAttributeOptions attributeOptions) {
        super(status, message);
        this.attributeOptions = attributeOptions;
    }
}
