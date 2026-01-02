package com.example.restapiversion.services;


import com.example.restapiversion.entities.Client;

import java.util.List;

public interface ClientService {

    Client createClient(Client client);
    Client getClientById(Long id);
    Client getClientByEmail(String email);
    List<Client> getAllClients();
    Client updateClient(Long id, Client request);
    void deleteClient(Long id);
    long count();

}
