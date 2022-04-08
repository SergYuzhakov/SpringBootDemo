package com.sbapp.todo.util;

import com.sbapp.todo.dto.ClientDto;
import com.sbapp.todo.dto.ToDoDto;
import com.sbapp.todo.model.Client;
import com.sbapp.todo.model.ToDo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DtoUtil {

    public static List<ToDoDto> createToDoDtoFromToDo(Collection<ToDo> toDos) {
        return toDos.stream().map(t -> new ToDoDto(t.getDescription(),
                        t.getCreated(),
                        t.getModified(),
                        t.isCompleted(),
                        t.getClient().getName()))
                .collect(Collectors.toList());
    }

    public static ClientDto createDtoClientFromClient(Client client) {
        return new ClientDto(client.getName()
                , client.getElAddress().getEmail()
                , client.getElAddress().getPhoneNumber()
                , client.getHomeAddress().getCity()
                , client.getHomeAddress().getStreet()
                , client.getHomeAddress().getHouseNumber().toString());
    }
}
