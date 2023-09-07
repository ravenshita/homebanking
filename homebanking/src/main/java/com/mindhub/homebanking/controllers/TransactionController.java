package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;

    @Transactional
    @PreAuthorize("hasAuthority('CLIENT')")
    @PostMapping("/transactions")

    public ResponseEntity<Object> transactions(
            @RequestParam String fromAccountNumber, @RequestParam String toAccountNumber,
            @RequestParam Double amount, @RequestParam String description,
            Authentication authentication) {

        if (amount == null) {
            return new ResponseEntity<>("Amount is missing.", HttpStatus.FORBIDDEN);
        }

        if (description == null) {
            return new ResponseEntity<>("Description is empty.", HttpStatus.FORBIDDEN);
        }

        if (fromAccountNumber == null || toAccountNumber == null) {
            return new ResponseEntity<>("Source or target account not found.", HttpStatus.FORBIDDEN);
        }

        if (amount <= 0 ) {
            return new ResponseEntity<>("Amount cannot be less then 0.", HttpStatus.FORBIDDEN);
        }

        Client client = clientRepository.findByEmail(authentication.getName());
        Account accountFromNumber = accountService.findByAccountNumber(fromAccountNumber);
        Account accountToNumber = accountService.findByAccountNumber(toAccountNumber);

        if (fromAccountNumber.equals(toAccountNumber)) {
            return new ResponseEntity<>("Source and target accounts cannot be the same.", HttpStatus.FORBIDDEN);
        }

        if (!accountFromNumber.getClient().equals(client)) {
            return new ResponseEntity<>("Source account does not belong to the authenticated client.", HttpStatus.FORBIDDEN);
        }

        if (accountFromNumber.getBalance() < amount) {
            return new ResponseEntity<>("Insufficient balance.", HttpStatus.FORBIDDEN);
        }

        Transaction debitTransaction = new Transaction(TransactionType.DEBIT, amount, description, LocalDateTime.now());
        accountFromNumber.addTransaction(debitTransaction);

        Transaction creditTransaction = new Transaction(TransactionType.CREDIT, amount, description, LocalDateTime.now());
        accountToNumber.addTransaction(creditTransaction);

        accountFromNumber.setBalance(accountFromNumber.getBalance() - amount);
        accountToNumber.setBalance(accountToNumber.getBalance() + amount);

        accountService.saveAccount(accountFromNumber);
        accountService.saveAccount(accountToNumber);
        transactionService.saveTransaction(debitTransaction);
        transactionService.saveTransaction(creditTransaction);

        return new ResponseEntity<>( HttpStatus.CREATED);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> getTransactionsForCurrentClient(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        List<TransactionDTO> transactionDTOs = client.getAccounts().stream()
                .flatMap(account -> account.getTransactions().stream())
                .map(TransactionDTO::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(transactionDTOs, HttpStatus.OK);
    }

}
