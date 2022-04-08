package com.sbapp.todo;

import com.sbapp.todo.model.ToDo;

import java.util.List;

public class ToDoUtil {
    private static final Iterable<ToDo> toDo = List.of(new ToDo(), new ToDo());
    private static final ToDo invalidToDo = new ToDo();

    public static Iterable<ToDo> getToDoTest() {
       toDo.forEach(todo -> todo.setDescription("Read a book"));
       //` toDo.setDescription("Read a book");
        toDo.forEach(todo -> todo.setClient(ClientUtil.getClientTest()));
        return toDo;
    }
    public static ToDo getInvalidToDo(){
        invalidToDo.setDescription("A");
        return invalidToDo;
    }

}
