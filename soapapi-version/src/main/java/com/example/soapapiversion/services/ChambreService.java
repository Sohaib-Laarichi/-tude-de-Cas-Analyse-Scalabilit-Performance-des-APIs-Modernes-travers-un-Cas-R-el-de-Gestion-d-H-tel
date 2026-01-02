package com.example.soapapiversion.services;




import com.example.soapapiversion.entities.Chambre;
import com.example.soapapiversion.entities.TypeChambre;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


public interface ChambreService {

    Chambre createChambre(Chambre chambre);
    Optional<Chambre> getChambreById(Long id);
    List<Chambre> getAllChambres();
    List<Chambre> getChambresByType(TypeChambre type);
    Chambre updateChambre(Long id, TypeChambre type, BigDecimal prix, Boolean disponible);
    void deleteChambre(Long id);
    long count();
}
