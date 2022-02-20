package com.sbapp.todo.repo;

import com.sbapp.todo.model.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientsJpaRepository extends CrudRepository<Client,Long> {

}
