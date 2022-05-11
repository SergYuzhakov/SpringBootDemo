package com.sbapp.todo.web.client;

import com.sbapp.annotations.TimeLogger;
import com.sbapp.todo.model.Client;
import com.sbapp.todo.service.ClientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class ClientRestController {
    private ClientService clientService;

    @GetMapping("/clients")
    @TimeLogger
    public ResponseEntity<?> getClients(@RequestParam("filter") String filter) {
        return ResponseEntity
                .ok(clientService.getAllClients(filter));
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        return ResponseEntity
                .ok(clientService.getClientById(id).get());
    }

    @RequestMapping(value = "/clients", method = {RequestMethod.POST,
            RequestMethod.PUT, RequestMethod.PATCH})
    @TimeLogger
    public ResponseEntity<?> updateClient(@Valid @RequestBody Client client) {
        Client updateClient = clientService.updateClient(client);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(updateClient.getId())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(updateClient);
    }

    @DeleteMapping("/clients/{id}")
    public ResponseEntity<Client> deleteClient(@PathVariable Long id) {
        clientService.deleteClientById(id);
        return ResponseEntity
                .noContent()
                .build();
    }


}
