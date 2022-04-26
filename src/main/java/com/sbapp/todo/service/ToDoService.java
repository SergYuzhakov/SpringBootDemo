package com.sbapp.todo.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbapp.AppConfig;
import com.sbapp.todo.dto.ToDoDto;
import com.sbapp.todo.model.Client;
import com.sbapp.todo.model.ToDo;
import com.sbapp.todo.repo.ClientsJpaRepository;
import com.sbapp.todo.repo.ToDoJpaRepository;
import com.sbapp.todo.util.DtoUtil;
import com.sbapp.todo.util.exception.JsonMappingHandlerException;
import com.sbapp.todo.util.exception.NoSuchElementFoundException;
import com.sbapp.todo.util.exception.SqlUniqueConstraintException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ToDoService {

    private final ToDoJpaRepository jpaRepository;
    private final ClientsJpaRepository clientsRepository;
    private ObjectMapper objectMapper;

    private DtoUtil dtoUtil;

    public List<ToDoDto> getAll(String filter, LocalDateTime fromDate, LocalDateTime toDate) {
        return dtoUtil.ToDoToDto(jpaRepository
                .findAllToDosWithClients(filter,
                        AppConfig.atStartOfDayOrMin(fromDate),
                        AppConfig.atStartOfDayOrMax(toDate)));
    }

    public Page<ToDoDto> findAllToDoDto(Pageable pageable, String filter,
                                        LocalDateTime fromDate, LocalDateTime toDate) {
        List<ToDoDto> dtoList = getAll(filter, fromDate, toDate);

        final int startPage = (int) pageable.getOffset();
        final int endPage = Math.min((startPage + pageable.getPageSize()), dtoList.size());

        return new PageImpl<>(dtoList.subList(startPage, endPage), pageable, dtoList.size());
    }

    public Collection<ToDoDto> getAllByClient(Long id) {
        return dtoUtil.ToDoToDto(jpaRepository
                .findAllToDosByClient(id));
    }

    public Optional<ToDo> getToDoById(Long id) {
        return Optional.ofNullable(jpaRepository
                .findToDoById(id).orElseThrow(() ->
                        new NoSuchElementFoundException(
                                String.format("ToDo with id = %d not found...", id))));
    }

    public Iterable<ToDoDto> getAllToDoDtoByClientName(String partName) {
        boolean isPartName = !partName.trim().isEmpty();
        return dtoUtil.ToDoToDto(jpaRepository
                .findAllToDosByClientNameLike(isPartName, partName));
    }

    @Transactional
    public ToDo updateToDo(ToDo toDo) {
        if (!toDo.isNew()) {
            ToDo oldTodo = getToDoById(toDo.getId()).get();
            Client oldToDoClient = oldTodo.getClient();
            toDo.setClient(oldToDoClient);
            try {
                objectMapper.updateValue(oldTodo, toDo);
            } catch (JsonMappingException e) {
                throw new JsonMappingHandlerException("Json mapping error(s).");
            }
            return jpaRepository.save(oldTodo);
        } else {
            Client client = toDo.getClient();
            Long clientId = null;
            String email = client.getElAddress().getEmail().toLowerCase();
            // здесь нужно идентифицировать клиента из базы по email (либо по номеру телефона)
            if (clientsRepository.existsClientByElAddress_Email(email)) {
                Optional<Client> fromDb = clientsRepository.getClientByEmailAddress(client.getElAddress().getEmail());
                if (fromDb.isPresent()) {
                    toDo.setClient(fromDb.get());
                }
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
        if (getToDoById(id).isPresent())
            jpaRepository.deleteById(id);
    }

}
