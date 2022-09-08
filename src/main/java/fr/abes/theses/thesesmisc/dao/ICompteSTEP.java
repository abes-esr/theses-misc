package fr.abes.theses.thesesmisc.dao;

import fr.abes.theses.thesesmisc.entities.CompteSTEP;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICompteSTEP extends JpaRepository<CompteSTEP, Integer> {
    CompteSTEP getCompteByIdDoc(Integer idDoc);
}
