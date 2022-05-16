package com.sbapp.todo.web.todo;

import com.sbapp.annotations.TimeLogger;
import com.sbapp.todo.dto.ToDoDto;
import com.sbapp.todo.model.ToDo;
import com.sbapp.todo.service.ToDoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.net.URI;
import java.time.LocalDateTime;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
@CrossOrigin(value = "http://localhost:3000")
@Slf4j
public class ToDoRestController {

    private final ToDoService toDoService;

    @GetMapping("/todo")
    @TimeLogger
    public ResponseEntity<?> getToDos(@RequestParam("filter") String filter,
                                      @RequestParam(required = false, value = "fromDate") LocalDateTime fromDate,
                                      @RequestParam(required = false, value = "toDate") LocalDateTime toDate) {
        return ResponseEntity.ok()
                .body(toDoService.getAll(filter, fromDate, toDate));
    }

    @GetMapping("/todoPageable")
    @TimeLogger
    // пример запроса http://localhost:8080/api/todoPageable?page=0&size=4&sort=created&filter=
    public ResponseEntity<Page<ToDoDto>> getPageableToDoDto(Pageable pageable, String filter,
                                                            @RequestParam(required = false, value = "fromDate")
                                                            LocalDateTime fromDate,
                                                            @RequestParam(required = false, value = "toDate")
                                                            LocalDateTime toDate) {
        return ResponseEntity.ok()
                .body(toDoService.findAllToDoDto(pageable, filter, fromDate, toDate));
    }

    @GetMapping("/todo/{id}")
    @TimeLogger
    public ResponseEntity<ToDo> getToDoById(@PathVariable Long id) {
        return ResponseEntity.ok(toDoService.getToDoById(id).get());
    }

    @GetMapping("/todo/client/{id}")
    @TimeLogger
    public ResponseEntity<?> findToDosByClient(@PathVariable Long id) {
        return ResponseEntity.ok(toDoService.getAllByClient(id));
    }

    @GetMapping("/todo/client")
    @TimeLogger
    public ResponseEntity<Iterable<ToDoDto>> findToDosByClientName(@RequestParam("partName") String partName) {
        return ResponseEntity.ok(toDoService.getAllToDoDtoByClientName(partName));
    }

    @RequestMapping(value = "/todo", method = {RequestMethod.PUT,
            RequestMethod.POST})
    public ResponseEntity<?> createToDo(@Valid @RequestBody ToDo toDo) {
        ToDo result = toDoService.create(toDo);
        // List<ToDoDto> dtoList = DtoUtil.createToDoDtoFromToDo(List.of(patchToDo));
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .buildAndExpand(result.getId())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(result);
    }

    @RequestMapping(value = "/todo/{id}",
            method = RequestMethod.PATCH)
    @TimeLogger
    public ResponseEntity<?> updateToDo(@PathVariable Long id,
                                        @RequestParam("description") @NotBlank @Size(min = 10, max = 100) String description,
                                        @RequestParam("completed") Boolean completed) {
        ToDo updateToDo = toDoService.update(id, description, completed);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .buildAndExpand(updateToDo.getId())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(updateToDo);
    }


    @DeleteMapping("/todo/{id}")
    public ResponseEntity<ToDo> deleteToDo(@PathVariable Long id) {
        toDoService.deleteToDoById(id);
        return ResponseEntity
                .noContent()
                .build();
    }

}
