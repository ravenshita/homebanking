package com.mindhub.homebanking.controllers;

import java.util.List;
import java.util.stream.Collectors;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.AccountDTO;


@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientRepository.findAll().stream().map(client -> {
            ClientDTO clientDTO = new ClientDTO(client);
            clientDTO.setAccounts(client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toSet()));
            return clientDTO;
        }).collect(Collectors.toList());
    }

    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        return clientRepository.findById(id).map(ClientDTO::new).orElse(null);
    }

}
