package com.sbapp.todo.service;

import com.sbapp.todo.model.Address;
import com.sbapp.todo.model.Client;
import com.sbapp.todo.model.ElAddress;
import com.sbapp.todo.model.ToDo;
import com.sbapp.todo.repo.ClientsJpaRepository;
import com.sbapp.todo.util.NotFoundException;
import com.sbapp.todo.util.ValidationUtil;
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
        Optional<Client> fromDb = clientsRepository.getClientByEmailAddress(client.getElAddress().getEmail());
        if(fromDb.isPresent()){
            log.info("Client already exist with id: {}", fromDb.get().getId());
            return fromDb.get();
        }
        return clientsRepository.save(client);

    }

    public void deleteClientById(Long id) {
        if (getClientById(id).isPresent()) {
            clientsRepository.deleteById(id);
        } else {
            throw new NotFoundException(String.format("Client with id = %d not found", id));
        }
    }

}
