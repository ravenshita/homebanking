package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.models.Loan;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


public interface LoanService {

    List<Loan> getAllLoans();
    Loan getLoanById(Long id);

}
