package com.sbapp.todo.service;

import com.sbapp.todo.dto.ToDoDto;
import com.sbapp.todo.model.Client;
import com.sbapp.todo.model.ElAddress;
import com.sbapp.todo.util.DtoUtil;
import com.sbapp.todo.util.ValidationUtil;
import com.sbapp.todo.model.ToDo;
import com.sbapp.todo.repo.ClientsJpaRepository;
import com.sbapp.todo.repo.ToDoJpaRepository;
import com.sbapp.todo.util.exception.NotFoundException;
import com.sbapp.todo.util.exception.SqlUniqueConstraintException;
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
        Long clientId = null;
        // здесь нужно идентифицировать клиента из базы по email
        Optional<Client> fromDb = clientsRepository.getClientByEmailAddress(client.getElAddress().getEmail());
        if (fromDb.isPresent()) {
            toDo.setClient(fromDb.get());
        } else {
            try {
                clientId = clientsRepository.save(toDo.getClient()).getId();
            } catch (Exception e) {
                throw new SqlUniqueConstraintException("Duplicate email or phone number");
            }
            toDo.setClient(clientsRepository.findById(clientId).get());
        }
        return jpaRepository.save(toDo);

    }

    @Transactional
    public void deleteToDoById(Long id) {
        try {
            jpaRepository.deleteById(id);
        }catch (Exception e){
            throw new NotFoundException(String.format("ToDo with id = %d not found", id));
        }
    }

}
