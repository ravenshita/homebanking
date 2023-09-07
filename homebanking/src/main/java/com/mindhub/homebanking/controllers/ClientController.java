package com.mindhub.homebanking.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
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
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;

    @GetMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientService.getAllClients();
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        return clientService.getClientById(id);
    }

    @PostMapping("/clients")
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

        if (clientService.getAuthenticatedClient(email) !=  null) {

            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);

        }
                 Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));
                 clientService.saveClient(newClient);

                 Random random = new Random();
                 String accountNumber = "VIN-" + (random.nextInt(900000) + 100000);

                 Account newAccount = new Account(accountNumber, LocalDate.now(), 0.0);
                 newAccount.setClient(newClient);
                 accountService.saveAccount(newAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @GetMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication) {
        return clientService.getAuthenticatedClient(authentication.getName());
    }

}
