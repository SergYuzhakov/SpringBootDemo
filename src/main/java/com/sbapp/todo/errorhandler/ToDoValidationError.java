package com.sbapp.todo.errorhandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ToDoValidationError {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> errors = new ArrayList<>();
    private final String errorMessage;

    public ToDoValidationError(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    public void addValidationError(String error) {
        errors.add(error);
    }

}
