package com.sbapp.todo.service;

import com.sbapp.AppConfig;
import com.sbapp.todo.dto.ToDoDto;
import com.sbapp.todo.model.Client;
import com.sbapp.todo.model.ToDo;
import com.sbapp.todo.repo.ClientsJpaRepository;
import com.sbapp.todo.repo.ToDoJpaRepository;
import com.sbapp.todo.util.DtoUtil;
import com.sbapp.todo.util.exception.NoSuchElementFoundException;
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

    public ToDo getToDoById(Long id) {
        return jpaRepository
                .findToDoById(id).orElseThrow(() ->
                        new NoSuchElementFoundException(
                                String.format("ToDo with id = %d not found...", id)));
    }

    public Iterable<ToDoDto> getAllToDoDtoByClientName(String partName) {
        return dtoUtil.ToDoToDto(jpaRepository
                .findAllToDosByClientNameLike(partName));
    }

    @Transactional
    public ToDo create(ToDo toDo) {
        Client client = toDo.getClient();
        if (!client.isNew()) {
            assert client.getId() != null;
            Optional<Client> fromDb = clientsRepository.findById(client.getId());
            fromDb.ifPresent(toDo::setClient);
        } else {
            Long clientId = clientsRepository.save(client).getId();
            assert clientId != null;
            clientsRepository.findById(clientId).ifPresent(toDo::setClient);

        }
        return jpaRepository.save(toDo);

    }

    @Transactional
    public ToDo update(Long id, String description, Boolean completed) {
        ToDo updateToDo = getToDoById(id);
        Client client = updateToDo.getClient();
        updateToDo.setCompleted(completed);
        updateToDo.setDescription(description);
        updateToDo.setClient(client);
        return updateToDo;
    }


    @Transactional
    public void deleteToDoById(Long id) {
        jpaRepository.deleteById(id);
    }

}
