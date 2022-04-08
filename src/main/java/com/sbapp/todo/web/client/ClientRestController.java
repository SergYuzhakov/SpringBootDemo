package com.sbapp.todo.web.client;

import com.sbapp.todo.dto.ClientDto;
import com.sbapp.todo.model.Client;
import com.sbapp.todo.service.ClientService;
import com.sbapp.todo.util.DtoUtil;
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
public class ClientRestController {
    private ClientService clientService;

    @GetMapping("/clients")
    public ResponseEntity<Iterable<Client>> getClients() {
        return ResponseEntity
                .ok(clientService.getAllClients());
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        return ResponseEntity
                .ok(clientService.getClientById(id).get());
    }

    @RequestMapping(value = "/clients", method = {RequestMethod.POST,
            RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<?> updateClient(@Valid @RequestBody Client client) {
        Client updateClient = clientService.updateClient(client);
        ClientDto clientDto = DtoUtil.createDtoClientFromClient(updateClient);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(updateClient.getId())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(clientDto);
    }

    @DeleteMapping("/clients/{id}")
    public ResponseEntity<Client> deleteClient(@PathVariable Long id) {
        clientService.deleteClientById(id);
        return ResponseEntity
                .noContent()
                .build();
    }


}
