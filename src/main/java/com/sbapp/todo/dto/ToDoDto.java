package com.sbapp.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ToDoDto implements Serializable {

    private String description;

    private LocalDateTime createdTodo;

    private LocalDateTime modifiedTodo;

    private boolean completed;

    private String clientName;

}
