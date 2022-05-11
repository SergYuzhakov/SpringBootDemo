package com.sbapp.todo.repo;

import com.sbapp.todo.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;

public interface ClientsJpaRepository extends JpaRepository<Client, Long> {

    @Query("""
            SELECT c FROM Client c
            WHERE 
            LOWER(c.name) LIKE CONCAT('%', :filter, '%')
            ORDER BY c.name ASC 
                        """)
    Collection<Client> fetchClients(String filter);


    @Query("""
            SELECT c
            FROM Client c
            WHERE c.elAddress.email=lower(:email)
            """)
    Optional<Client> getClientByEmailAddress(String email);

    boolean existsClientByElAddress_Email(String email);
}
