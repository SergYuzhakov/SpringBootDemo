package com.sbapp.todo;

import com.sbapp.todo.dto.ToDoDto;
import com.sbapp.todo.model.ToDo;
import com.sbapp.todo.util.DtoUtil;

import java.util.List;

public class ToDoUtil {
    private static final Iterable<ToDo> toDos = List.of(new ToDo(), new ToDo());
    private static final ToDo toDoWithId = new ToDo();
    private static final ToDo invalidToDo = new ToDo();

    public static ToDo getToDoWithIdTest() {
        toDoWithId.setDescription("Read a book");
        toDoWithId.setId(1L);
        toDoWithId.setClient(ClientUtil.getClientWithIdTest());
        return toDoWithId;
    }

    public static Iterable<ToDo> getToDosTest() {
        toDos.forEach(t -> t.setDescription("Read a book"));
        toDos.forEach(t -> t.setClient(ClientUtil.getClientTest()));
        return toDos;
    }

    public static ToDo getInvalidToDo() {
        invalidToDo.setDescription("A");
        return invalidToDo;
    }



}
