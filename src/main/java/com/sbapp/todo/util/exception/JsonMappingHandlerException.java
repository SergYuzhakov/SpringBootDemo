package com.sbapp.todo.util.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;

public class JsonMappingHandlerException extends AppException {
    public JsonMappingHandlerException(String message) {
        super(HttpStatus.NOT_MODIFIED, message, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE));
    }
}
