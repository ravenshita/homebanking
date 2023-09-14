package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private ClientService clientService;

    @Autowired
    AccountService accountService;

    @Autowired
    LoanService loanService;

    @Autowired
    ClientLoanService clientLoanService;

    @Autowired
    TransactionService transactionService;

    @Transactional
    @PreAuthorize("hasAuthority('CLIENT')")
    @PostMapping(value ="/loans")
    public ResponseEntity<Object> applyForLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {

        Loan loan = loanService.getLoanById(loanApplicationDTO.getLoanId());
        Account accountToNumber = accountService.findByAccountNumber(loanApplicationDTO.getToAccountNumber());
        Client client = clientService.getAuthenticatedClient(authentication);

        if (loanApplicationDTO.getAmount() <= 0) {
            return new ResponseEntity<>("Amount is less then zero.", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.getPayments() <= 0) {
            return new ResponseEntity<>("Payments is less then zero.", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.getToAccountNumber().isEmpty()) {
            return new ResponseEntity<>("Target account is empty.", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.getAmount().compareTo(loan.getMaxAmount()) > 0) {
            return new ResponseEntity<>("Loan amount exceeds the maximum allowed", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.getLoanId() == null) {
            return new ResponseEntity<>("Loan not found", HttpStatus.NOT_FOUND);
        }

        if (!loan.getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("Invalid number of payments for this loan type", HttpStatus.BAD_REQUEST);
        }

        if (accountToNumber == null) {
            return new ResponseEntity<>("Target account does not exist.", HttpStatus.BAD_REQUEST);
        }

        if (!accountToNumber.getClient().equals(client)) {
            return new ResponseEntity<>("Target account does not belong to the authenticated client.", HttpStatus.BAD_REQUEST);
        }

        double totalLoan = loanApplicationDTO.getAmount() * 1.20;
        Transaction creditTransaction = new Transaction(TransactionType.CREDIT, totalLoan, "Loan: " + loan.getName() + " loan approved", LocalDateTime.now());

        accountToNumber.addTransaction(creditTransaction);
        accountService.saveAccount(accountToNumber);
        transactionService.saveTransaction(creditTransaction);
        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount(), loanApplicationDTO.getPayments());
        loan.addClientLoan(clientLoan);
        client.addClientLoan(clientLoan);
        clientLoanService.saveClientLoan(clientLoan);

        return new ResponseEntity<>( HttpStatus.CREATED);

    }
    @GetMapping("/loans")
    public List<LoanDTO> getLoans() {return loanService.getAllLoans(); }

}