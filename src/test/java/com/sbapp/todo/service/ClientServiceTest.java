package com.sbapp.todo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbapp.annotations.TimeLogger;
import com.sbapp.todo.ClientUtil;
import com.sbapp.todo.ToDoUtil;
import com.sbapp.todo.model.Client;
import com.sbapp.todo.repo.ClientsJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@Slf4j
class ClientServiceTest {

    @Autowired
    private ClientService clientService;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private ClientsJpaRepository clientsJpaRepository;

    private Client savedClient;
    private Client client;

    @BeforeEach
    void initSetup() throws JsonProcessingException {
        client = ClientUtil.getClientTest();
        String testClient = objectMapper.writeValueAsString(client);
        savedClient = objectMapper.readValue(testClient, Client.class);
        savedClient.setId(2L);
    }

    @Test
    @DisplayName("Test Created Client")
    void createClient() {
        // Setup our mock repository
        doReturn(savedClient).when(clientsJpaRepository).save(any());
        // Execute the service call
        Client returnedClient = clientService.createClient(client);
        log.info("Save Client: {}", returnedClient);
        Assertions.assertNotNull(returnedClient, "The saved client should not be null");
        Assertions.assertEquals("Jon", returnedClient.getName());
    }
}