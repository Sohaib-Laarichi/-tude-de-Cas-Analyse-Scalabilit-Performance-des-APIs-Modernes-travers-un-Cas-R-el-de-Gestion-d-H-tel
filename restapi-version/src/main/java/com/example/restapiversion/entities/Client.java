package com.example.restapiversion.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    @Column(nullable = false)
    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;
    @Column(nullable = false)
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Email invalide")
    private String email;
    @Column(nullable = false)
    @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;
}
