package com.sbapp.todo.repo;

import com.sbapp.todo.model.ToDo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
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
             WHERE 
                   (t.created >= :fromDate AND t.created <= :toDate)
                AND
                   (LOWER(t.client.name) LIKE CONCAT('%', LOWER(:filter), '%') 
                   OR LOWER(t.description) LIKE CONCAT('%',LOWER(:filter),'%' ))
            ORDER BY t.created DESC
                         
             """
    )
    Collection<ToDo> findAllToDosWithClients( String filter,
                                             LocalDateTime fromDate, LocalDateTime toDate);

    @Query("""
            SELECT t FROM ToDo t
            JOIN FETCH t.client
            WHERE t.client.id=:id
                                    """)
    Collection<ToDo> findAllToDosByClient(@Param("id") Long id);

    @Query("""
            SELECT t FROM ToDo t
            JOIN FETCH t.client
            WHERE 
            LOWER(t.client.name) LIKE CONCAT('%', LOWER(:partName), '%')
            ORDER BY t.created DESC
                        
             """)
    Collection<ToDo> findAllToDosByClientNameLike(String partName);

}
