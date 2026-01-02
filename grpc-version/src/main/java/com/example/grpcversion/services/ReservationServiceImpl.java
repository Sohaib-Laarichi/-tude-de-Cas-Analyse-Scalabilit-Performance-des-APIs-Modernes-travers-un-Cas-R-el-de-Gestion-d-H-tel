package com.example.grpcversion.services;

import com.example.grpcversion.entities.Chambre;
import com.example.grpcversion.entities.Client;
import com.example.grpcversion.entities.Reservation;
import com.example.grpcversion.repositories.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ClientService clientService;
    private final ChambreService chambreService;


    @Override
    public Reservation createReservation(Reservation reservation) {
        log.debug("Création d'une réservation pour le client {} et la chambre {} du {} au {}",
                reservation.getClient().getNom(), reservation.getChambre().getId(), reservation.getDateDebut(), reservation.getDateFin());

        // Validation des dates
        if (reservation.getDateDebut().isAfter(reservation.getDateFin())) {
            throw new IllegalArgumentException("La date de début doit être avant la date de fin");
        }

        if (reservation.getDateDebut().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La date de début ne peut pas être dans le passé");
        }
// Récupérer le client
        Client client = clientService.getClientById(reservation.getClient().getId());
        // Récupérer la chambre
        Chambre chambre = chambreService.getChambreById(reservation.getChambre().getId())
                .orElseThrow(() -> new IllegalArgumentException("Chambre non trouvée avec l'ID : " + reservation.getChambre().getId()));

        // Vérifier la disponibilité de la chambre pour la période demandée
        List<Reservation> reservationsConflict = reservationRepository.findConflictingReservations(
                chambre.getId(),
                reservation.getDateDebut(),
                reservation.getDateFin()
        );

        if (!reservationsConflict.isEmpty()) {
            throw new IllegalArgumentException("La chambre n'est pas disponible pour la période demandée");
        }

        // Créer la réservation
        Reservation res = new Reservation();
        res.setClient(client);
        res.setChambre(chambre);
        res.setDateDebut(reservation.getDateDebut());
        res.setDateFin(reservation.getDateFin());
        res.setPreferences(reservation.getPreferences());

        Reservation savedReservation = reservationRepository.save(res);

        return savedReservation;
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Reservation> getReservationById(Long id) {
        log.debug("Récupération de la réservation avec l'ID : {}", id);
        return reservationRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getAllReservations() {
        log.debug("Récupération de toutes les réservations");
        return reservationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getReservationsByClientId(Long clientId) {
        log.debug("Récupération des réservations pour le client : {}", clientId);
        return reservationRepository.findByClientId(clientId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getReservationsByChambreId(Long chambreId) {
        log.debug("Récupération des réservations pour la chambre : {}", chambreId);
        return reservationRepository.findByChambreId(chambreId);
    }

    @Override
    public Reservation updateReservation(Long id, Long clientId, Long chambreId, LocalDate dateDebut, LocalDate dateFin, String preferences) {
        log.debug("Mise à jour de la réservation avec l'ID : {}", id);

        // Validation des dates
        if (dateDebut.isAfter(dateFin)) {
            throw new IllegalArgumentException("La date de début doit être avant la date de fin");
        }

        // Récupérer la réservation existante
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Réservation non trouvée avec l'ID : " + id));

        // Si les dates changent OU la chambre change, vérifier la disponibilité
        if (!reservation.getChambre().getId().equals(chambreId)
                || !reservation.getDateDebut().equals(dateDebut)
                || !reservation.getDateFin().equals(dateFin)) {

            // Vérifier la disponibilité de la chambre pour la nouvelle période
            // Exclure la réservation actuelle du conflit (si on garde la même chambre)
            List<Reservation> reservationsConflict = reservationRepository.findConflictingReservations(
                            chambreId,
                            dateDebut,
                            dateFin
                    ).stream()
                    .filter(r -> !r.getId().equals(id)) // Exclure la réservation actuelle
                    .toList();

            if (!reservationsConflict.isEmpty()) {
                throw new IllegalArgumentException("La chambre n'est pas disponible pour la période demandée");
            }
        }

        // Récupérer le nouveau client
        Client client = clientService.getClientById(clientId);

        // Récupérer la nouvelle chambre
        Chambre chambre = chambreService.getChambreById(chambreId)
                .orElseThrow(() -> new IllegalArgumentException("Chambre non trouvée avec l'ID : " + chambreId));


        // Mettre à jour la réservation
        reservation.setClient(client);
        reservation.setChambre(chambre);
        reservation.setDateDebut(dateDebut);
        reservation.setDateFin(dateFin);
        reservation.setPreferences(preferences);

        return reservationRepository.save(reservation);
    }

    @Override
    public void deleteReservation(Long id) {
        log.debug("Suppression de la réservation avec l'ID : {}", id);

        reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Réservation non trouvée avec l'ID : " + id));

        reservationRepository.deleteById(id);

    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return reservationRepository.count();
    }
}
