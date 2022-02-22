package com.sbapp.todo.controller;

import com.sbapp.todo.errorhandler.ToDoValidationError;
import com.sbapp.todo.errorhandler.ToDoValidationErrorBuilder;
import com.sbapp.todo.model.Client;
import com.sbapp.todo.model.ElAddress;
import com.sbapp.todo.model.ToDo;
import com.sbapp.todo.repo.ClientsJpaRepository;
import com.sbapp.todo.repo.ToDoJpaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
@Slf4j
public class ToDoController {
    private ToDoJpaRepository repository;
    private ClientsJpaRepository clientsRepository;

    @GetMapping("/todo")
    public ResponseEntity<Iterable<ToDo>> getToDos() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/todo/{id}")
    public ResponseEntity<ToDo> getToDoById(@PathVariable Long id) {
        if (!getToDoFromRepo(id).isPresent()) {
            return notFound();
        }
        return ResponseEntity.ok(getToDoFromRepo(id).get());
    }

    @PatchMapping("/todo/{id}")
    public ResponseEntity<ToDo> setCompleted(@PathVariable Long id) {
        if (!getToDoFromRepo(id).isPresent()) {
            return notFound();
        }
        ToDo result = getToDoFromRepo(id).get();
        result.setCompleted(true);
        repository.save(result);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .buildAndExpand(result.getId())
                .toUri();
        return ResponseEntity.ok()
                .header("Location", location.toString())
                .build();
    }

    @RequestMapping(value = "/todo", method = {RequestMethod.POST})
    public ResponseEntity<?> createToDo(@Valid @RequestBody ToDo toDo,
                                        Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().
                    body(ToDoValidationErrorBuilder.fromBindingErrors(errors));
        }

        Client client = toDo.getClient();
        ElAddress address = client.getElAddress();
        Optional<Client> fromDb = clientsRepository.getClientByEmailAddress(address.getEmail());

        if (fromDb.isPresent()) {
            toDo.setClient(getClientFromRepo(fromDb.get().getId()));
        } else {
            clientsRepository.save(client);
            toDo.setClient(getClientFromRepo(client.getId()));
        }

        ToDo result = repository.save(toDo);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @RequestMapping(value = "/todo", method = {RequestMethod.PUT})
    public ResponseEntity<?> updateToDo(@Valid @RequestBody ToDo toDo,
                                        Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().
                    body(ToDoValidationErrorBuilder.fromBindingErrors(errors));
        }

        Long todoId = toDo.getId();
        ToDo oldTodo = getToDoFromRepo(todoId).get();
        Long clientId = oldTodo.getClient().getId();
        toDo.setClient(getClientFromRepo(clientId));

        ToDo result = repository.save(toDo);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/todo/{id}")
    public ResponseEntity<ToDo> deleteToDo(@PathVariable Long id) {
        if (!getToDoFromRepo(id).isPresent()) {
            return notFound();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/todo")
    public ResponseEntity<ToDo> deleteToDo() {
        Iterable<ToDo> toDos = repository.findAll();
        repository.deleteAll(toDos);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ToDoValidationError handleException(Exception exception) {
        return new ToDoValidationError(exception.getMessage());
    }

    private Optional<ToDo> getToDoFromRepo(Long id) {
        return repository.findById(id);
    }

    private Client getClientFromRepo(Long id){
        return clientsRepository.findById(id).get();
    }

    private ResponseEntity<ToDo> notFound() {
        return ResponseEntity.notFound().build();
    }


}
