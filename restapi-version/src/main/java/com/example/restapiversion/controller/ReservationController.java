package com.example.restapiversion.controller;

import com.example.restapiversion.entities.Reservation;
import com.example.restapiversion.services.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Reservation> create(@RequestBody Reservation reservation) {
        Reservation created = reservationService.createReservation(reservation);
        return ResponseEntity.ok(created);
    }

    // READ all
    @GetMapping
    public List<Reservation> getAll() {
        return reservationService.getAllReservations();
    }

    // READ by id
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getById(@PathVariable Long id) {
        return reservationService.getReservationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE (dates + client/chambre par id)
    @PutMapping("/{id}")
    public ResponseEntity<Reservation> update(@PathVariable Long id, @RequestBody Reservation reservation) {
        Reservation updated = reservationService.updateReservation(
                id,
                reservation.getClient().getId(),
                reservation.getChambre().getId(),
                reservation.getDateDebut(),
                reservation.getDateFin(),
                reservation.getPreferences());
        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
