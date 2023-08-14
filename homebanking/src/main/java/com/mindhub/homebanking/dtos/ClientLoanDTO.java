package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;
import java.util.stream.Collectors;

public class ClientLoanDTO {
    private Long id;

    private Double amount;
    private Integer payments;


    public ClientLoanDTO (ClientLoan clientLoan){

        this.id = clientLoan.getId();

        this.amount = clientLoan.getAmount();

        this.payments = clientLoan.getPayments();


    }

    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

}
