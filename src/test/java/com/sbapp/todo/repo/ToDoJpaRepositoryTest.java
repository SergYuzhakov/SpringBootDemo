package com.sbapp.todo.repo;

import com.sbapp.todo.ClientUtil;
import com.sbapp.todo.ToDoUtil;
import com.sbapp.todo.model.Client;
import com.sbapp.todo.model.ToDo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
class ToDoJpaRepositoryTest {

    @Autowired
    ToDoJpaRepository toDoRepository;

    @Autowired
    ClientsJpaRepository clientRepository;

    private Iterable<ToDo> toDos = ToDoUtil.getToDosTest();
    private Client client = ClientUtil.getClientTest();

    @BeforeEach
    void setUp() {
        clientRepository.save(client);
        toDoRepository.saveAll(toDos);

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void findToDoById() {
        Optional<ToDo> toDoFromDb = this.toDoRepository.findToDoById(1001L);
        log.info("Restored from DB: {}", toDoFromDb);
        assertEquals("Read a book", toDoFromDb.get().getDescription());

    }

    @Test
    void findAllToDoWithClients() {
        Iterable<ToDo> toDos = this.toDoRepository.findAllToDosWithClients( "", null, null);
        assertEquals(2, toDos.spliterator().estimateSize());
        assertEquals("Read a book", toDos.iterator().next().getDescription());

    }

    @Test
    void findAllToDoByClient() {
        Iterable<ToDo> toDos = this.toDoRepository.findAllToDosByClient(1000L);
        assertEquals(2, toDos.spliterator().estimateSize());
        assertEquals("Read a book", toDos.iterator().next().getDescription());

    }

    @Test
    void findAllToDoByClientNameLike() {
        Iterable<ToDo> toDos = this.toDoRepository.findAllToDosByClientNameLike(true,"jo");
        assertEquals(2, toDos.spliterator().estimateSize());
        assertEquals("Jon", toDos.iterator().next().getClient().getName());

    }

    @Test
    void deleteToDoById() {
        toDoRepository.deleteById(1001L);
        assertFalse(toDoRepository.existsById(1001L));

    }

    @Test
    void deleteClientById() {
        clientRepository.deleteById(1000L);
        Iterable<ToDo> toDos = this.toDoRepository.findAllToDosByClient(1000L);
        assertEquals(0, toDos.spliterator().estimateSize());
        assertFalse(toDoRepository.existsById(1001L));
        assertFalse(clientRepository.existsById(1000L));


    }
}
