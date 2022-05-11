package com.sbapp.todo.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbapp.todo.dto.ClientDto;
import com.sbapp.todo.model.Client;
import com.sbapp.todo.repo.ClientsJpaRepository;
import com.sbapp.todo.util.DtoUtil;
import com.sbapp.todo.util.ValidationUtil;
import com.sbapp.todo.util.exception.JsonMappingHandlerException;
import com.sbapp.todo.util.exception.NoSuchElementFoundException;
import com.sbapp.todo.util.exception.NotFoundException;
import com.sbapp.todo.util.exception.SqlUniqueConstraintException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ClientService {
    private final ClientsJpaRepository clientsRepository;
    private ObjectMapper objectMapper;

    private DtoUtil dtoUtil;

    public Collection<ClientDto> getAllClients(String filter) {
        String query = filter.trim();
        return dtoUtil.ClientToDto(clientsRepository.fetchClients(filter));
    }

    public Optional<Client> getClientById(Long id) {
        return Optional.ofNullable(clientsRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementFoundException(
                                String.format("ToDo with id = %d not found...", id))));
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
        if (getClientById(id).isPresent())
            clientsRepository.deleteById(id);
    }

}
