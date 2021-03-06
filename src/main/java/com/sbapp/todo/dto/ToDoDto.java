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
    @Setter
    private String description;

    private LocalDateTime createdTodo;

    private LocalDateTime modifiedTodo;
    @Setter
    private boolean completed;

    private String clientName;

    private Long clientId;

}
