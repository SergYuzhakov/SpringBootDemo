package com.sbapp.todo.service;

import com.sbapp.todo.model.Client;
import com.sbapp.todo.repo.ClientsJpaRepository;
import com.sbapp.todo.util.ValidationUtil;
import com.sbapp.todo.util.exception.NotFoundException;
import com.sbapp.todo.util.exception.SqlUniqueConstraintException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ClientService {
    private final ClientsJpaRepository clientsRepository;

    public Iterable<Client> getAllClients() {
        return clientsRepository.findAll();
    }

    public Optional<Client> getClientById(Long id) {
        return ValidationUtil.checkObjectIsPresent(clientsRepository.findById(id));
    }

    public Client updateClient(Client client, Long id) {
        Client oldClient = getClientById(id).get();
        oldClient.setName(client.getName());
        oldClient.setHomeAddress(client.getHomeAddress());
        oldClient.setElAddress(client.getElAddress());

        return clientsRepository.save(oldClient);
    }

    public Client createClient(Client client) {
        try {
            return clientsRepository.save(client);
        } catch (Exception e) {
            throw new SqlUniqueConstraintException("Client with this email and phone already exist!");
        }
    }

    public void deleteClientById(Long id) {
        try {
            clientsRepository.deleteById(id);
        } catch (Exception e) {
            throw new NotFoundException(String.format("Client with id = %d not found", id));
        }
    }

}
