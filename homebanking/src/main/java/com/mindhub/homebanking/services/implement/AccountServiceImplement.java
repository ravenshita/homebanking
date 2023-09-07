package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImplement implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;


    @Override
    public List<AccountDTO> getAccounts() {
        return accountRepository.findAll().stream().map( account ->
                new AccountDTO(account)
        ).collect(Collectors.toList());
    }

    @Override
    public AccountDTO getAccount(Long id) {
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }

    @Override
    public List<AccountDTO> getAccountsForCurrentClient(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        return client.getAccounts().stream()
                .map(AccountDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public Account findByAccountNumber(String accountNumber) {
        return accountRepository.findByNumber(accountNumber);
    }

    @Override
    public void saveAccount(Account newAccount) {
        accountRepository.save(newAccount);
    }

}
