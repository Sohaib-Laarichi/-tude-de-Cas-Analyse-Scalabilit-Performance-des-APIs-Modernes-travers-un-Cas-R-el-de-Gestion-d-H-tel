package com.example.graphqlversion.repositories;

import com.example.graphqlversion.entities.Chambre;
import com.example.graphqlversion.entities.TypeChambre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ChambreRepository extends JpaRepository<Chambre, Long> {
    List<Chambre> findByType(TypeChambre type);
}
