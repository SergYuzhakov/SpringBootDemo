package com.sbapp.todo.repo;

import com.sbapp.todo.model.Client;
import com.sbapp.todo.model.ElAddress;
import org.springframework.data.repository.CrudRepository;

public interface ClientsJpaRepository extends CrudRepository<Client,Long> {

    public Client getClientByElAddress(ElAddress address);

}
