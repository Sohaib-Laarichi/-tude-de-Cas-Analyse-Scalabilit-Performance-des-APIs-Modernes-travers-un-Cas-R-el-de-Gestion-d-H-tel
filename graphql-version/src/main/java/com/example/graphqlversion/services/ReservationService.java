package com.example.graphqlversion.services;




import com.example.graphqlversion.entities.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface ReservationService {

    Reservation createReservation(Reservation reservation);
    Optional<Reservation> getReservationById(Long id);
    List<Reservation> getAllReservations();
    List<Reservation> getReservationsByClientId(Long clientId);
    List<Reservation> getReservationsByChambreId(Long chambreId);
    Reservation updateReservation(Long id, Long clientId, Long chambreId, LocalDate dateDebut, LocalDate dateFin, String preferences);
    void deleteReservation(Long id);
    long count();

}
