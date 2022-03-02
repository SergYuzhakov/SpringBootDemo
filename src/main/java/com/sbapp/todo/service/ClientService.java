package com.sbapp.todo.service;

import com.sbapp.todo.model.Client;
import com.sbapp.todo.model.ToDo;
import com.sbapp.todo.repo.ClientsJpaRepository;
import com.sbapp.todo.util.ValidationUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ClientService {
    private final ClientsJpaRepository clientsRepository;

    public Optional<Client> getClientById(Long id){
        return ValidationUtil.checkObjectIsPresent(clientsRepository.findById(id));
    }

}
