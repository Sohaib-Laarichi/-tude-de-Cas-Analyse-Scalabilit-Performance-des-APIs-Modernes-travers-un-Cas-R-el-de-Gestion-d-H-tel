package com.example.restapiversion.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @NotNull(message = "Le client est obligatoire")
    private Client client;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "La chambre est obligatoire")
    @JoinColumn(name = "chambre_id", nullable = false)
    private Chambre chambre;
    @NotNull(message = "La date de début est obligatoire")
    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;
    @NotNull(message = "La date de fin est obligatoire")
    @Column(name = "date_fin", nullable = false)
    private LocalDate dateFin;
    @Column(length = 500)
    private String preferences;
}
