package com.example.grpcversion.repositories;

import com.example.grpcversion.entities.Chambre;
import com.example.grpcversion.entities.TypeChambre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ChambreRepository extends JpaRepository<Chambre, Long> {
    List<Chambre> findByType(TypeChambre type);
}
