package com.sbapp.todo.controller;

import com.sbapp.todo.errorhandler.ToDoValidationErrorBuilder;
import com.sbapp.todo.model.Client;
import com.sbapp.todo.repo.ClientsJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ClientController {
    private ClientsJpaRepository clientsJpaRepository;

    @GetMapping("/clients")
    public ResponseEntity<Iterable<Client>> getClients() {
        return ResponseEntity.ok(clientsJpaRepository.findAll());
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        if (!getClientFromDb(id).isPresent()) {
            return notFound();
        }
        return ResponseEntity.ok(getClientFromDb(id).get());
    }

    @RequestMapping(value = "/clients", method = {RequestMethod.POST,
            RequestMethod.PUT})
    public ResponseEntity<?> createClient(@Valid @RequestBody Client client,
                                          Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().
                    body(ToDoValidationErrorBuilder.fromBindingErrors(errors));
        }
        Client result = clientsJpaRepository.save(client);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/clients/{id}")
    public ResponseEntity<Client> deleteToDo(@PathVariable Long id) {
        if (!getClientFromDb(id).isPresent()) {
            return notFound();
        }
        clientsJpaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Optional<Client> getClientFromDb(Long id) {
        return clientsJpaRepository.findById(id);
    }

    private ResponseEntity<Client> notFound() {
        return ResponseEntity.notFound().build();
    }

}
