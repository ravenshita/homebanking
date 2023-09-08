package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private ClientService clientservice;

    @Autowired
    AccountService accountService;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountService.getAccounts();
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return accountService.getAccount(id);
    }

    @PreAuthorize("hasAuthority('CLIENT')")
    @PostMapping("/clients/current/accounts")

    public ResponseEntity<Object> createAccount(Authentication authentication) {
        Client client = clientservice.getAuthenticatedClient(authentication);

        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("Client already has 3 accounts", HttpStatus.FORBIDDEN);
        }

        Random random = new Random();

        String accountNumber = "VIN-" + (random.nextInt(900000) + 100000);

        Account newAccount = new Account(accountNumber, LocalDate.now(), 0.0 );
        newAccount.setClient(client);
        accountService.saveAccount(newAccount);

        AccountDTO newAccountDTO = new AccountDTO(newAccount);
        return new ResponseEntity<>(newAccountDTO, HttpStatus.CREATED);

    }

    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getAccountsForCurrentClient(Authentication authentication) {
        return accountService.getAccountsForCurrentClient(authentication);
    }

}