package com.sbapp.todo.web.todo;

import com.sbapp.todo.errorhandler.ToDoValidationErrorBuilder;
import com.sbapp.todo.model.ToDo;
import com.sbapp.todo.service.ToDoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
@Slf4j
public class ToDoRestController {

    private final ToDoService toDoService;

    @GetMapping("/todo")
    public ResponseEntity<Iterable<ToDo>> getToDos() {
        return ResponseEntity.ok(toDoService.getAll());
    }

    @GetMapping("/todo/{id}")
    public ResponseEntity<ToDo> getToDoById(@PathVariable Long id) {
        return ResponseEntity.ok(toDoService.getToDoById(id).get());
    }

    @GetMapping("/todo/client/{id}")
    public ResponseEntity<Iterable<ToDo>> findToDosByClient(@PathVariable Long id) {
        return ResponseEntity.ok(toDoService.getAllByClient(id));
    }

    @PatchMapping("/todo/{id}")
    public ResponseEntity<ToDo> setCompleted(@PathVariable Long id) {
        ToDo patchToDo = toDoService.setCompleted(id);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .buildAndExpand(patchToDo.getId())
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
        ToDo result = toDoService.createToDo(toDo);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/todo/{id}")
    public ResponseEntity<ToDo> deleteToDo(@PathVariable Long id) {
        toDoService.deleteToDoById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/todo")
    public ResponseEntity<ToDo> deleteToDo() {
        toDoService.deleteAllToDo();
        return ResponseEntity.noContent().build();
    }

}
