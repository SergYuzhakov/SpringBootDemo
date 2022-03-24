package com.sbapp.todo.web.todo;

import com.sbapp.todo.dto.ToDoDto;
import com.sbapp.todo.model.ToDo;
import com.sbapp.todo.service.ToDoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/todo/client/search")
    public ResponseEntity<Iterable<ToDoDto>> findToDosByClientName(@RequestParam("partName") String partName) {
        return ResponseEntity.ok(toDoService.getAllToDoDtoByClientName(partName));
    }

    @RequestMapping(value = "/todo/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> setCompleted(@Valid @RequestBody ToDo toDo,
                                          @PathVariable Long id) {
        ToDo patchToDo = toDoService.updateToDo(toDo, id);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .buildAndExpand(patchToDo.getId())
                .toUri();
        return ResponseEntity.ok()
                .header("Location", location.toString())
                .build();
    }

    @RequestMapping(value = "/todo", method = {RequestMethod.POST})
    public ResponseEntity<?> createToDo(@Valid @RequestBody ToDo toDo) {

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
