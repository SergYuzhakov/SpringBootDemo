package com.sbapp.todo.model;

public class ToDoBuilder {
    private static ToDoBuilder instance = new ToDoBuilder();
    private String id = null;
    private String description = "";
    private ToDoBuilder(){}
    public static ToDoBuilder create() {
        return instance;
    }
    public ToDoBuilder withDescription(String description){
        this.description = description;
        return instance;
    }
    public ToDoBuilder withId(String id){
        this.id = id;
        return instance;
    }
    /* При использовании Data JPA надобность в этой фабрике отпадет

    public ToDo build(){
        ToDo result = new ToDo(this.description);
        if(id != null)
            result.setId(id);
        return result;
    }

     */
}
