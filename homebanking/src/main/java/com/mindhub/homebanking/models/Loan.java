package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private Long id;
    private String name;
    private Double maxAmount;

    @ElementCollection
    private List<Integer> payments = new ArrayList<>();

    @OneToMany(mappedBy = "loan")
    private List<ClientLoan> clients;

    public Loan(){}

    public Loan (String name, Double maxAmount, List<Integer>payments) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
    }

    public void addClientLoan(ClientLoan clientLoan) {
        clients.add(clientLoan);
        clientLoan.setLoan(this);
    }

    public List<Integer> getPayments() {return payments;}

    public List<ClientLoan> getClients() {
        return clients;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }

}
