package com.sbapp.todo.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbapp.todo.model.Client;
import com.sbapp.todo.repo.ClientsJpaRepository;
import com.sbapp.todo.util.ValidationUtil;
import com.sbapp.todo.util.exception.JsonMappingHandlerException;
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
    private ObjectMapper objectMapper;

    public Iterable<Client> getAllClients() {
        return clientsRepository.findAll();
    }

    public Optional<Client> getClientById(Long id) {
        return ValidationUtil.checkObjectIsPresent(clientsRepository.findById(id));
    }

    public Client updateClient(Client client) {
        if (!client.isNew()) {
            Client oldClient = getClientById(client.getId()).get();
            try {
                objectMapper.updateValue(oldClient, client);
            } catch (JsonMappingException e) {
                throw new JsonMappingHandlerException("Json mapping error(s).");
            }
            return clientsRepository.save(oldClient);
        } else {
            try {
                log.info("Client to service: {}", client);
                Client newClient = clientsRepository.save(client);
                return newClient;
            } catch (Exception e) {
                throw new SqlUniqueConstraintException("Client with this email and phone already exist!");
            }
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
