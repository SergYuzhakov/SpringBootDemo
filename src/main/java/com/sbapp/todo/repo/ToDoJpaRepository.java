package com.sbapp.todo.repo;

import com.sbapp.todo.model.ToDo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ToDoJpaRepository extends CrudRepository<ToDo, Long> {

    @Query("""
            SELECT t FROM ToDo t
            JOIN FETCH Client c 
            ON t.client.id = c.id
            """
    )


    Iterable<ToDo> findAllToDoWithClients();

}
