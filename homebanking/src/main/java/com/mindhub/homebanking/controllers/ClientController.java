package com.mindhub.homebanking.controllers;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.AccountDTO;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/clients")
    public List<ClientDTO> getClients() {
            return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
    }

    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        return clientRepository.findById(id).map(ClientDTO::new).orElse(null);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    /*@RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<Object> login(
            @RequestParam String email, @RequestParam String password) {

        return new ResponseEntity<>(HttpStatus.OK);
    }*/

    @RequestMapping(path = "/clients", method = RequestMethod.POST)

    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password)

             {

        List<String> errorMessages = new ArrayList<>();

        if (firstName == null || firstName.isEmpty()) {
            errorMessages.add("First name is missing");
        }

        if (lastName == null || lastName.isEmpty()) {
            errorMessages.add("Last name is missing");
        }

        if (email == null || email.isEmpty()) {
            errorMessages.add("Email is missing");
        }

        if (password == null || password.isEmpty()) {
            errorMessages.add("Password is missing");
        }

        if (!errorMessages.isEmpty()) {
            return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
        }

        if (clientRepository.findByEmail(email) !=  null) {

            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);

        }

        clientRepository.save(new Client(firstName, lastName, email, passwordEncoder.encode(password)));

        return new ResponseEntity<>(HttpStatus.CREATED);

    }
    @RequestMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        return new ClientDTO(client);
    }


}


