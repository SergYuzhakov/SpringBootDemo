package com.sbapp.todo.repo;

import com.sbapp.todo.model.ToDo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ToDoJpaRepository extends PagingAndSortingRepository<ToDo, Long> {

    @Query("""
            SELECT t FROM ToDo t
            JOIN FETCH t.client
            WHERE t.id=:id
            """
    )
    Optional<ToDo> findToDoById(@Param("id") Long id);


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
