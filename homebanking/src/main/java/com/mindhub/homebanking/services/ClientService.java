package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;


import java.util.List;

public interface ClientService {

    List<ClientDTO> getAllClients();

    ClientDTO getClientById(Long id);

    Client getClientByEmail(String email);

    ClientDTO getAuthenticatedClientDTO(Authentication authentication);

    Client getAuthenticatedClient(Authentication authentication);

    void saveClient(Client newClient);
}