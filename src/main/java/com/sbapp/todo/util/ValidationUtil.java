package com.sbapp.todo.util;

public class ValidationUtil {

    public static <T> T checkObjectIsPresent(T object) {
        if (object == null) {
            throw new NotFoundException("Object not found");
        }
        return object;
    }
}
