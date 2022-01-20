package com.sbapp.todo.controller;

import com.sbapp.todo.errorhandler.ToDoValidationError;
import com.sbapp.todo.errorhandler.ToDoValidationErrorBuilder;
import com.sbapp.todo.model.ToDo;
import com.sbapp.todo.repo.ToDoJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ToDoController {
    private ToDoJpaRepository repository;

    public ToDoController(ToDoJpaRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/todo")
    public ResponseEntity<Iterable<ToDo>> getToDos() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/todo/{id}")
    public ResponseEntity<ToDo> getToDoById(@PathVariable Integer id) {
        if (!getToDo(id).isPresent()) {
            return notFound();
        }
        return ResponseEntity.ok(getToDo(id).get());
    }

    @PatchMapping("/todo/{id}")
    public ResponseEntity<ToDo> setCompleted(@PathVariable Integer id) {
        if (!getToDo(id).isPresent()) {
            return notFound();
        }
        ToDo result = getToDo(id).get();
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

    @RequestMapping(value = "/todo", method = {RequestMethod.POST,
            RequestMethod.PUT})
    public ResponseEntity<?> createToDo(@Valid @RequestBody ToDo toDo,
                                        Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().
                    body(ToDoValidationErrorBuilder.fromBindingErrors(errors));
        }
        ToDo result = repository.save(toDo);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/todo/{id}")
    public ResponseEntity<ToDo> deleteToDo(@PathVariable Integer id) {
        if (!getToDo(id).isPresent()) {
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

    private Optional<ToDo> getToDo(Integer id) {
        return repository.findById(id);
    }

    private ResponseEntity<ToDo> notFound(){
        return ResponseEntity.notFound().build();
    }


}
