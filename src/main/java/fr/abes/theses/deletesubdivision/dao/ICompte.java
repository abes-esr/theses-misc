package fr.abes.theses.deletesubdivision.dao;

import fr.abes.theses.deletesubdivision.entities.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ICompte extends JpaRepository<Compte, Integer> {

    Compte getCompteByIdDoc(Integer idDoc);
}
