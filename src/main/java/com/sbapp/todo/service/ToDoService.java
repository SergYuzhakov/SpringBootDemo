package com.sbapp.todo.service;

import com.sbapp.todo.model.Client;
import com.sbapp.todo.model.ElAddress;
import com.sbapp.todo.util.ValidationUtil;
import com.sbapp.todo.model.ToDo;
import com.sbapp.todo.repo.ClientsJpaRepository;
import com.sbapp.todo.repo.ToDoJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ToDoService {

    private final ToDoJpaRepository jpaRepository;
    private final ClientsJpaRepository clientsRepository;

    public Iterable<ToDo> getAll() {
        return jpaRepository.findAll();
    }

    public Optional<ToDo> getToDoById(Long id) {
        return ValidationUtil.checkObjectIsPresent(jpaRepository.findById(id));
    }

    public ToDo setCompleted(Long id) {
        ToDo result = getToDoById(id).get();
        result.setCompleted(true);
        return jpaRepository.save(result);
    }

    public ToDo createToDo(ToDo toDo) {
        Client client = toDo.getClient();
        ElAddress address = client.getElAddress();
        Optional<Client> fromDb = clientsRepository.getClientByEmailAddress(address.getEmail());
        if (fromDb.isPresent()) {
            toDo.setClient(clientsRepository.findById(Objects.requireNonNull(fromDb.get().getId())).get());
        } else {
            clientsRepository.save(client);
            assert client.getId() != null;
            toDo.setClient(clientsRepository.findById(client.getId()).get());
        }
        return jpaRepository.save(toDo);
    }

    public ToDo updateToDo(ToDo toDo) {
        Long todoId = toDo.getId();
        ToDo oldTodo = getToDoById(todoId).get();
        Long clientId = oldTodo.getClient().getId();
        toDo.setClient(clientsRepository.findById(clientId).get());
        return jpaRepository.save(toDo);
    }

    public void deleteToDoById(Long id) {
        jpaRepository.delete(getToDoById(id).get());
    }

    public void deleteAllToDo() {
        Iterable<ToDo> toDos = getAll();
        jpaRepository.deleteAll(toDos);
    }

}
