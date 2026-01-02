package com.example.restapiversion.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


@Entity
@Table(name = "chambres")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Builder
public class Chambre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @NotNull(message = "Le type est obligatoire")
    @Enumerated(EnumType.STRING)
    private TypeChambre type;
    @Column(nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Le prix est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit etre positif")
    private BigDecimal prix;
    @OneToMany(mappedBy = "chambre", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Reservation> reservations;
}
