package com.sbapp.todo.web.todo;

import com.sbapp.annotations.TimeLogger;
import com.sbapp.todo.dto.ToDoDto;
import com.sbapp.todo.model.ToDo;
import com.sbapp.todo.service.ToDoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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
    @TimeLogger
    public ResponseEntity<Iterable<ToDo>> getToDos() {
        return ResponseEntity.ok(toDoService.getAll());
    }

    @GetMapping("/todoPageable")
    @TimeLogger
    // пример запроса http://localhost:8080/api/todoPageable?page=1&size=4&sort=created
    public ResponseEntity<?> getPageableToDoDto(Pageable pageable){
        return ResponseEntity.ok().body(toDoService.findAllToDoDto(pageable));
    }

    @GetMapping("/todo/{id}")
    @TimeLogger
    public ResponseEntity<ToDo> getToDoById(@PathVariable Long id) {
        return ResponseEntity.ok(toDoService.getToDoById(id).get());
    }

    @GetMapping("/todo/client/{id}")
    @TimeLogger
    public ResponseEntity<Iterable<ToDo>> findToDosByClient(@PathVariable Long id) {
        return ResponseEntity.ok(toDoService.getAllByClient(id));
    }

    @GetMapping("/todo/client/search")
    @TimeLogger
    public ResponseEntity<Iterable<ToDoDto>> findToDosByClientName(@RequestParam("partName") String partName) {
        return ResponseEntity.ok(toDoService.getAllToDoDtoByClientName(partName));
    }

    @RequestMapping(value = "/todo", method = {RequestMethod.PUT,
            RequestMethod.POST, RequestMethod.PATCH})
    public ResponseEntity<?> setCompleted(@Valid @RequestBody ToDo toDo) {
        ToDo patchToDo = toDoService.updateToDo(toDo);
        // List<ToDoDto> dtoList = DtoUtil.createToDoDtoFromToDo(List.of(patchToDo));
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .buildAndExpand(patchToDo.getId())
                .toUri();
        return ResponseEntity.created(location)
                .body(patchToDo);
    }

    @DeleteMapping("/todo/{id}")
    public ResponseEntity<ToDo> deleteToDo(@PathVariable Long id) {
        toDoService.deleteToDoById(id);
        return ResponseEntity.noContent().build();
    }

}
