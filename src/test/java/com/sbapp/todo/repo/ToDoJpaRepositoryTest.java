package com.sbapp.todo.repo;

import com.sbapp.todo.ClientUtil;
import com.sbapp.todo.ToDoUtil;
import com.sbapp.todo.model.Client;
import com.sbapp.todo.model.ToDo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;


@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
class ToDoJpaRepositoryTest {

    @Autowired
    ToDoJpaRepository repository;

    @Autowired
    ClientsJpaRepository clientsJpaRepository;

    private Iterable<ToDo> toDO = ToDoUtil.getToDoTest();
    private Client client = ClientUtil.getClientTest();

    @BeforeEach
    void setUp() {
        clientsJpaRepository.save(client);
        toDO.forEach(t-> repository.save(t));
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void findToDoById() {

        Optional<ToDo> fromDb = repository.findToDoById(1001L);
        log.info(fromDb.toString());
        Assertions.assertTrue(fromDb.isPresent());
    }

    @Test
    void findAllToDoWithClients() {
        Iterable<ToDo> allToDos = repository.findAllToDoWithClients();
        Assertions.assertTrue(allToDos.iterator().next().getDescription().equals("Read a book"));
    }

    @Test
    void findAllToDoByClient() {
    }

    @Test
    void findAllToDoByClientNameLike() {
    }
    @Test
    void deleteToDoById(){
        repository.deleteById(toDO.iterator().next().getId());
        Iterable<ToDo> allToDos = repository.findAllToDoWithClients();
        Assertions.assertFalse(allToDos.iterator().hasNext());
    }
    @Test
    void deleteClientById() {
        clientsJpaRepository.deleteById(client.id());
        Iterable<ToDo> allToDos = repository.findAllToDoWithClients();
        List<Client> allClients = clientsJpaRepository.findAll();
        Assertions.assertTrue(allClients.isEmpty());
        Assertions.assertFalse(allToDos.iterator().hasNext());

    }
}
