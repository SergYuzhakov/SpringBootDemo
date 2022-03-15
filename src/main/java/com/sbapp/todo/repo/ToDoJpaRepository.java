package com.sbapp.todo.repo;

import com.sbapp.todo.model.ToDo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ToDoJpaRepository extends CrudRepository<ToDo, Long> {

    @Query("""
            SELECT t FROM ToDo t
            JOIN FETCH t.client
            """
    )
    Iterable<ToDo> findAllToDoWithClients();

    @Query("""
            SELECT t FROM ToDo t
            JOIN FETCH t.client
            WHERE t.client.id=:id
                                    """)
    Iterable<ToDo> findAllToDoByClient(@Param("id") Long id);

    @Query("""
            SELECT t FROM ToDo t
            JOIN FETCH t.client
            WHERE LOWER(t.client.name) LIKE LOWER(CONCAT('%', :partName, '%')) 
                                    """)
    Iterable<ToDo> findAllToDoByClientNameLike(@Param("partName") String partName);

}
