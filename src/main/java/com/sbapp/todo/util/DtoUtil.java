package com.sbapp.todo.util;

import com.sbapp.todo.dto.ClientDto;
import com.sbapp.todo.dto.ToDoDto;
import com.sbapp.todo.model.Client;
import com.sbapp.todo.model.ToDo;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DtoUtil {

    public ToDoDto createToDoDto(ToDo entity) {
        return ToDoDto.builder()
                .id(entity.id())
                .description(entity.getDescription())
                .createdTodo(entity.getCreated())
                .modifiedTodo(entity.getModified())
                .completed(entity.isCompleted())
                .clientName(entity.getClient().getName())
                .clientId(entity.getClient().id())
                .build();
    }


    public ClientDto createClientDto(Client entity) {
        return ClientDto.builder()
                .id(entity.id())
                .name(entity.getName())
                .email(entity.getElAddress().getEmail())
                .build();
    }

    public List<ToDoDto> ToDoToDto(Collection<ToDo> toDos) {
        return toDos.stream().map(this::createToDoDto)
                .collect(Collectors.toList());

    }

    public List<ClientDto> ClientToDto(Collection<Client> client) {
        return client.stream().map(this::createClientDto)
                .collect(Collectors.toList());
    }

}
