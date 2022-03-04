package com.sbapp.todo.web.client;

import com.sbapp.todo.errorhandler.ToDoValidationErrorBuilder;
import com.sbapp.todo.model.Client;
import com.sbapp.todo.repo.ClientsJpaRepository;
import com.sbapp.todo.service.ClientService;
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
public class ClientRestController {
    private ClientService clientService;

    @GetMapping("/clients")
    public ResponseEntity<Iterable<Client>> getClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientById(id).get());
    }

    @RequestMapping(value = "/clients", method = {RequestMethod.POST,
            RequestMethod.PUT})
    public ResponseEntity<?> createClient(@Valid @RequestBody Client client,
                                          Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().
                    body(ToDoValidationErrorBuilder.fromBindingErrors(errors));
        }
        Client result = clientService.createClient(client);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/clients/{id}")
    public ResponseEntity<Client> deleteClient(@PathVariable Long id) {
        clientService.deleteClientById(id);
        return ResponseEntity.noContent().build();
    }


}
