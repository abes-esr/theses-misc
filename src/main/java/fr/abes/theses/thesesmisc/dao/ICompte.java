package fr.abes.theses.thesesmisc.dao;

import fr.abes.theses.thesesmisc.entities.Compte;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICompte extends JpaRepository<Compte, Integer> {

    Compte getCompteByIdDoc(Integer idDoc);
}
