package com.sbapp.todo.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbapp.todo.dto.ClientDto;
import com.sbapp.todo.model.Client;
import com.sbapp.todo.repo.ClientsJpaRepository;
import com.sbapp.todo.util.DtoUtil;
import com.sbapp.todo.util.exception.JsonMappingHandlerException;
import com.sbapp.todo.util.exception.NoSuchElementFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@AllArgsConstructor
@Slf4j
public class ClientService {
    private final ClientsJpaRepository clientsRepository;
    private ObjectMapper objectMapper;
    private DtoUtil dtoUtil;

    public Collection<ClientDto> getAllClients(String filter) {
        String query = filter.trim();
        return dtoUtil.ClientToDto(clientsRepository.fetchClients(query));
    }

    public Client getClientById(Long id) {
        return clientsRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementFoundException(
                                String.format("ToDo with id = %d not found...", id)));
    }

    @Transactional
    public Client createClient(Client client) {
        if (!client.isNew()) {
            Client updateClient = getClientById(client.getId());
            try {
                objectMapper.updateValue(updateClient, client);
            } catch (JsonMappingException e) {
                throw new JsonMappingHandlerException("Json mapping error(s).");
            }
            return updateClient;
        } else {
            log.info("Client to service: {}", client);
            return clientsRepository.save(client);
        }
    }

    @Transactional
    public void deleteClientById(Long id) {
        clientsRepository.deleteById(id);
    }

}
