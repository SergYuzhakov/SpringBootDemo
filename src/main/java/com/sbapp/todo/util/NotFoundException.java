package com.sbapp.todo.util;

public class NotFoundException  extends RuntimeException{
    public NotFoundException (String messge) {
        super(messge);
    }
}
