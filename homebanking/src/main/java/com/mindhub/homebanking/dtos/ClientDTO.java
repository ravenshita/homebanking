package com.mindhub.homebanking.dtos;

import java.util.List;
import java.util.Set;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import java.util.stream.Collectors;

public class ClientDTO {

        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private Set<AccountDTO> accounts;
        private List<ClientLoanDTO> loans;
        private List<CardDTO> cards;

        public ClientDTO(Client client) {

                this.id = client.getId();

                this.firstName = client.getFirstName();

                this.lastName = client.getLastName();

                this.email = client.getEmail();

                this.password = client.getPassword();

                this.accounts = client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toSet());

                this.loans = client.getLoans().stream().map(ClientLoanDTO::new).collect(Collectors.toList());

                this.cards = client.getCards().stream().map(CardDTO::new).collect(Collectors.toList());

        }

        public Long getId() {
                return id;
        }

        public String getFirstName() {
                return firstName;
        }

        public String getLastName() {
                return lastName;
        }

        public String getEmail() {
                return email;
        }

        public String getPassword() {
                return password;
        }

        public Set<AccountDTO> getAccounts() {
                return accounts;
        }

        public List<ClientLoanDTO> getLoans() {
                return loans;
        }
        public List<CardDTO> getCards() {
                return cards;
        }

        public void setAccounts(Set<AccountDTO> accounts) {
                this.accounts = accounts;
        }

}