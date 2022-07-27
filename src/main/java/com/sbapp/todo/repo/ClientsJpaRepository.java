package com.sbapp.todo.repo;

import com.sbapp.todo.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface ClientsJpaRepository extends CrudRepository<Client, Long> {

    @Query("""
            SELECT c FROM Client c
            WHERE 
            LOWER(c.name) LIKE CONCAT('%', LOWER(:filter), '%')
            ORDER BY c.name ASC 
                        """)
    Collection<Client> fetchClients(String filter);


    @Query("""
            SELECT c
            FROM Client c
            WHERE c.elAddress.email=LOWER(:email) 
            """)
    Optional<Client> getClientByEmailAddress(String email);

    boolean existsClientByElAddress_Email(String email);
}
