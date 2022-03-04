package com.sbapp.todo.service;

import com.sbapp.todo.model.Client;
import com.sbapp.todo.model.ToDo;
import com.sbapp.todo.repo.ClientsJpaRepository;
import com.sbapp.todo.util.NotFoundException;
import com.sbapp.todo.util.ValidationUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ClientService {
    private final ClientsJpaRepository clientsRepository;

    public Iterable<Client> getAllClients() {
        return clientsRepository.findAll();
    }

    public Optional<Client> getClientById(Long id){
        return ValidationUtil.checkObjectIsPresent(clientsRepository.findById(id));
    }
    public Client createClient(Client client){
        Optional<Client> clientDb = clientsRepository.getClientByEmailAddress(client.getElAddress().getEmail());
        if(!clientDb.isPresent())
            return clientsRepository.save(client);
        else {
            clientDb.stream().forEach(c -> c.setName(client.getName()));
            return clientsRepository.save(clientDb.get());
        }
    }

    public void deleteClientById(Long id){
        if(getClientById(id).isPresent()) {
            clientsRepository.deleteById(id);
        }
        else {
            throw new NotFoundException(String.format("Client with id = %d not found", id));
        }
    }


}
