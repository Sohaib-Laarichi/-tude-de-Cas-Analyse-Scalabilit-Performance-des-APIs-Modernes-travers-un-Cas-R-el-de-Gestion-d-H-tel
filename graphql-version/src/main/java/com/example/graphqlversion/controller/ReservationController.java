package com.example.graphqlversion.controller;

import com.example.graphqlversion.entities.Chambre;
import com.example.graphqlversion.entities.Client;
import com.example.graphqlversion.entities.Reservation;
import com.example.graphqlversion.services.ReservationService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class ReservationGraphqlController {

    private final ReservationService reservationService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");

    public ReservationGraphqlController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @QueryMapping
    public Reservation reservationById(@Argument Long id) {
        return reservationService.getReservationById(id).orElse(null);
    }

    @QueryMapping
    public List<Reservation> allReservations() {
        return reservationService.getAllReservations();
    }

    @MutationMapping
    public Reservation createReservation(@Argument ReservationInput reservation) {
        com.example.graphqlversion.entities.Reservation entity = new com.example.graphqlversion.entities.Reservation();
        
        Client client = new Client();
        client.setId(reservation.clientId());
        
        Chambre chambre = new Chambre();
        chambre.setId(reservation.chambreId());
        
        entity.setClient(client);
        entity.setChambre(chambre);
        entity.setDateDebut(LocalDate.parse(reservation.dateDebut(), formatter));
        entity.setDateFin(LocalDate.parse(reservation.dateFin(), formatter));
        entity.setPreferences(reservation.preferences());
        
        return reservationService.createReservation(entity);
    }

    @MutationMapping
    public Reservation updateReservation(@Argument Long id, @Argument ReservationInput reservation) {
        return reservationService.updateReservation(
                id,
                reservation.clientId(),
                reservation.chambreId(),
                LocalDate.parse(reservation.dateDebut(), formatter),
                LocalDate.parse(reservation.dateFin(), formatter),
                reservation.preferences
        );
    }

    @MutationMapping
    public Boolean deleteReservation(@Argument Long id) {
        reservationService.deleteReservation(id);
        return true;
    }

    record ReservationInput(Long clientId, Long chambreId, String dateDebut, String dateFin, String preferences) {}
}
