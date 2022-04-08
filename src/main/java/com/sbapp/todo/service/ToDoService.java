package com.sbapp.todo.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbapp.todo.dto.ToDoDto;
import com.sbapp.todo.model.Client;
import com.sbapp.todo.model.ToDo;
import com.sbapp.todo.repo.ClientsJpaRepository;
import com.sbapp.todo.repo.ToDoJpaRepository;
import com.sbapp.todo.util.DtoUtil;
import com.sbapp.todo.util.ValidationUtil;
import com.sbapp.todo.util.exception.JsonMappingHandlerException;
import com.sbapp.todo.util.exception.NotFoundException;
import com.sbapp.todo.util.exception.SqlUniqueConstraintException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ToDoService {

    private final ToDoJpaRepository jpaRepository;
    private final ClientsJpaRepository clientsRepository;
    private ObjectMapper objectMapper;

    public Iterable<ToDo> getAll() {
        return jpaRepository.findAllToDoWithClients();
    }

    public Page<ToDoDto> findAllToDoDto(Pageable pageable){
        List<ToDo> toDoList = new ArrayList<>();
        getAll().forEach(toDoList::add);
        List<ToDoDto> dtoList = DtoUtil.createToDoDtoFromToDo(toDoList);

        final int startPage = (int) pageable.getOffset();
        final int endPage = Math.min((startPage + pageable.getPageSize()),dtoList.size());

        return new PageImpl<>(dtoList.subList(startPage, endPage),pageable,dtoList.size());



    }

    public Iterable<ToDo> getAllByClient(Long id) {
        return jpaRepository.findAllToDoByClient(id);
    }

    public Optional<ToDo> getToDoById(Long id) {
        return ValidationUtil.checkObjectIsPresent(jpaRepository.findToDoById(id));
    }

    public Iterable<ToDoDto> getAllToDoDtoByClientName(String partName) {
        return DtoUtil.createToDoDtoFromToDo((Collection<ToDo>) jpaRepository.findAllToDoByClientNameLike(partName));
    }

    @Transactional
    public ToDo updateToDo(ToDo toDo)  {
        if(!toDo.isNew()) {
            ToDo oldTodo = getToDoById(toDo.getId()).get();
            Client oldToDoClient = oldTodo.getClient();
            toDo.setClient(oldToDoClient);
            try {
                objectMapper.updateValue(oldTodo, toDo);
            } catch (JsonMappingException e) {
                throw new JsonMappingHandlerException("Json mapping error(s).");
            }
            return jpaRepository.save(oldTodo);
        }else {
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
    }

    @Transactional
    public void deleteToDoById(Long id) {
        try {
            jpaRepository.deleteById(id);
        } catch (Exception e) {
            throw new NotFoundException(String.format("ToDo with id = %d not found", id));
        }
    }

}
