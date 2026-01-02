package com.example.grpcversion.repositories;

import com.example.grpcversion.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByClientId(Long clientId);

    List<Reservation> findByChambreId(Long chambreId);

    @Query("SELECT r FROM Reservation r WHERE r.chambre.id = :chambreId " +
            "AND ((r.dateDebut BETWEEN :dateDebut AND :dateFin) " +
            "OR (r.dateFin BETWEEN :dateDebut AND :dateFin) " +
            "OR (:dateDebut BETWEEN r.dateDebut AND r.dateFin))")
    List<Reservation> findConflictingReservations(
            @Param("chambreId") Long chambreId,
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin
    );
}
