package com.mindhub.homebanking.dtos;

import java.util.Set;
import com.mindhub.homebanking.models.Client;
import java.util.stream.Collectors;

public class ClientDTO {

        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private Set<AccountDTO> accounts;

        public ClientDTO(Client client) {

                this.id = client.getId();

                this.firstName = client.getFirstName();

                this.lastName = client.getLastName();

                this.email = client.getEmail();

                this.accounts = client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toSet());

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

        public Set<AccountDTO> getAccounts() {
                return accounts;
        }

        public void setAccounts(Set<AccountDTO> accounts) {
                this.accounts = accounts;
        }
}
