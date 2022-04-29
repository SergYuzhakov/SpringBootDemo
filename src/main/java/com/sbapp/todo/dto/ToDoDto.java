package com.sbapp.todo.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ToDoDto implements Serializable {
    private Long id;

    private String description;

    private LocalDateTime createdTodo;

    private LocalDateTime modifiedTodo;

    private boolean completed;

    private String clientName;

    private Long clientId;

}
