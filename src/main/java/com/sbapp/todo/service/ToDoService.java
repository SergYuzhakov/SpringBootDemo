package com.sbapp.todo.service;

import com.sbapp.todo.dto.ToDoDto;
import com.sbapp.todo.model.Client;
import com.sbapp.todo.model.ElAddress;
import com.sbapp.todo.util.DtoUtil;
import com.sbapp.todo.util.ValidationUtil;
import com.sbapp.todo.model.ToDo;
import com.sbapp.todo.repo.ClientsJpaRepository;
import com.sbapp.todo.repo.ToDoJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ToDoService {

    private final ToDoJpaRepository jpaRepository;
    private final ClientsJpaRepository clientsRepository;

    public Iterable<ToDo> getAll() {
        return jpaRepository.findAllToDoWithClients();
    }

    public Iterable<ToDo> getAllByClient(Long id) {
        return jpaRepository.findAllToDoByClient(id);
    }

    public Optional<ToDo> getToDoById(Long id) {
        return ValidationUtil.checkObjectIsPresent(jpaRepository.findById(id));
    }

    public Iterable<ToDoDto> getAllToDoDtoByClientName(String partName) {
        return DtoUtil.createToDoDtoFromToDo((Collection<ToDo>) jpaRepository.findAllToDoByClientNameLike(partName));
    }

    @Transactional
    public ToDo updateToDo(ToDo toDo, Long id) {
        ToDo oldTodo = getToDoById(id).get();
        oldTodo.setCompleted(toDo.isCompleted());
        oldTodo.setDescription(toDo.getDescription());
        return jpaRepository.save(oldTodo);
    }

    @Transactional
    public ToDo createToDo(ToDo toDo) {
        Client client = toDo.getClient();
        ElAddress address = client.getElAddress();
        Optional<Client> fromDb = clientsRepository.getClientByEmailAddress(address.getEmail());
        if (fromDb.isPresent()) {
            toDo.setClient(fromDb.get());
        } else {
            Long newId = clientsRepository.save(client).getId();
            toDo.setClient(clientsRepository.findById(newId).get());
        }
        return jpaRepository.save(toDo);
    }

    @Transactional
    public void deleteToDoById(Long id) {
        jpaRepository.delete(getToDoById(id).get());
    }

    public void deleteAllToDo() {
        Iterable<ToDo> toDos = getAll();
        jpaRepository.deleteAll(toDos);
    }

}
