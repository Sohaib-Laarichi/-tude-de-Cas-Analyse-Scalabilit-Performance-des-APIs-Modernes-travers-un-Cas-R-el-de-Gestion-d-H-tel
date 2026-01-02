package com.example.grpcversion.services;

import com.example.grpcversion.entities.Chambre;
import com.example.grpcversion.entities.TypeChambre;
import com.example.grpcversion.repositories.ChambreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ChambreServiceImpl implements ChambreService {

    private final ChambreRepository chambreRepository;

    @Override
    public Chambre createChambre(Chambre chambre) {
        log.debug("Création d'une nouvelle chambre de type " + chambre.getType() + " à " + chambre.getPrix() + " DH");

        Chambre savedChambre = new Chambre();
        savedChambre.setType(chambre.getType());
        savedChambre.setPrix(chambre.getPrix());

        return chambreRepository.save(chambre);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Chambre> getChambreById(Long id) {
        log.debug("Récupération de la chambre avec l'ID : {}", id);
        return chambreRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Chambre> getAllChambres() {
        log.debug("Récupération de toutes les chambres");
        return chambreRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Chambre> getChambresByType(TypeChambre type) {
        log.debug("Récupération des chambres de type : {}", type);
        return chambreRepository.findByType(type);
    }

    @Override
    public Chambre updateChambre(Long id, TypeChambre type, BigDecimal prix, Boolean disponible) {
        log.debug("Mise à jour de la chambre avec l'ID : {}", id);

        Chambre chambre = chambreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chambre non trouvée avec l'ID : " + id));

        chambre.setType(type);
        chambre.setPrix(prix);

        return chambreRepository.save(chambre);
    }

    @Override
    public void deleteChambre(Long id) {
        log.debug("Suppression de la chambre avec l'ID : {}", id);

        if (!chambreRepository.existsById(id)) {
            throw new IllegalArgumentException("Chambre non trouvée avec l'ID : " + id);
        }

        chambreRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return chambreRepository.count();
    }
}
