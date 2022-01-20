package com.sbapp.todo.repo;

import com.sbapp.todo.model.ToDo;
import org.springframework.data.repository.CrudRepository;

public interface ToDoJpaRepository extends CrudRepository<ToDo, Integer> {
}
