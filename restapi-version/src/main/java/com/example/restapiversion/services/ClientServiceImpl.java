package com.example.restapiversion.services;

import com.example.restapiversion.entities.Client;
import com.example.restapiversion.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;


    @Override
    public Client createClient(Client client) {
        log.debug("Création d'un nouveau client : {} {}", client.getPrenom(), client.getNom());

        // Vérifier si l'email existe déjà
        clientRepository.findByEmail(client.getEmail()).ifPresent(existingClient -> {
            throw new IllegalArgumentException("Un client avec cet email existe déjà");
        });

        Client savedClient = new Client();
        savedClient.setNom(client.getNom());
        savedClient.setPrenom(client.getPrenom());
        savedClient.setEmail(client.getEmail());
        savedClient.setTelephone(client.getTelephone());

        return clientRepository.save(savedClient);
    }

    @Override
    @Transactional(readOnly = true)
    public Client getClientById(Long id) {
        log.debug("Récupération du client avec l'ID : {}", id);
        return clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client non trouvé avec l'ID : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Client getClientByEmail(String email) {
        log.debug("Récupération du client avec l'email : {}", email);
        return clientRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Client non trouvé avec l'email : " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> getAllClients() {
        log.debug("Récupération de tous les clients");
        return clientRepository.findAll();
    }

    @Override
    public Client updateClient(Long id, Client request) {
        log.debug("Mise à jour du client avec l'ID : {}", id);

        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client non trouvé avec l'ID : " + id));

        // Vérifier si le nouvel email est déjà utilisé par un autre client
        if (!client.getEmail().equals(request.getEmail())) {
            clientRepository.findByEmail(request.getEmail()).ifPresent(existingClient -> {
                throw new IllegalArgumentException("Un autre client utilise déjà cet email");
            });
        }

        client.setNom(request.getNom());
        client.setPrenom(request.getPrenom());
        client.setEmail(request.getEmail());
        client.setTelephone(request.getTelephone());

        return clientRepository.save(client);
    }

    @Override
    public void deleteClient(Long id) {
        log.debug("Suppression du client avec l'ID : {}", id);

        if (!clientRepository.existsById(id)) {
            throw new IllegalArgumentException("Client non trouvé avec l'ID : " + id);
        }

        clientRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return clientRepository.count();
    }
}
