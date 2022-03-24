package com.sbapp.todo.util;

import com.sbapp.todo.util.exception.NoSuchElementFoundException;
import com.sbapp.todo.util.exception.NotFoundException;

import java.util.Optional;

public class ValidationUtil {

    public static <T> Optional checkObjectIsPresent(Optional<T> object) {
        if (object.isEmpty()) {
            throw new NoSuchElementFoundException("Object not found");
        }
        return object;
    }
}
