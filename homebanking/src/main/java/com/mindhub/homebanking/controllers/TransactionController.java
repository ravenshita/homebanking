package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
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
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Transactional
    @PreAuthorize("hasAuthority('CLIENT')")
    @PostMapping("/transactions")

    public ResponseEntity<Object> register(
            @RequestParam String fromAccountNumber, @RequestParam String toAccountNumber,
            @RequestParam Double amount, @RequestParam String description,
            Authentication authentication) {

        Client client = clientRepository.findByEmail(authentication.getName());
        Account accountFromNumber = accountRepository.findByNumber(fromAccountNumber);
        Account accountToNumber = accountRepository.findByNumber(toAccountNumber);


        if (amount == null) {
            return new ResponseEntity<>("Amount is missing.", HttpStatus.FORBIDDEN);
        }

        if (description == null) {
            return new ResponseEntity<>("Description is empty.", HttpStatus.FORBIDDEN);
        }

        if (fromAccountNumber == null || toAccountNumber == null) {
            return new ResponseEntity<>("Source or target account not found.", HttpStatus.FORBIDDEN);
        }

        if (fromAccountNumber.equals(toAccountNumber)) {
            return ResponseEntity.badRequest().body("Source and target accounts cannot be the same.");
        }

        if (!accountFromNumber.getClient().equals(client)) {
            return new ResponseEntity<>("Source account does not belong to the authenticated client.", HttpStatus.FORBIDDEN);
        }

        if (accountFromNumber.getBalance() < amount) {
            return new ResponseEntity<>("Insufficient balance.", HttpStatus.FORBIDDEN);
        }

        Transaction debitTransaction = new Transaction(TransactionType.DEBITO, amount, description, LocalDateTime.now());
        accountFromNumber.addTransaction(debitTransaction);

        Transaction creditTransaction = new Transaction(TransactionType.CREDITO, amount, description, LocalDateTime.now());
        accountToNumber.addTransaction(creditTransaction);

        accountFromNumber.setBalance(accountFromNumber.getBalance() - amount);
        accountToNumber.setBalance(accountToNumber.getBalance() + amount);

        accountRepository.save(accountFromNumber);
        accountRepository.save(accountToNumber);
        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);

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
