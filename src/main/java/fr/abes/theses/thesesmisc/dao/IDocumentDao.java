package fr.abes.theses.thesesmisc.dao;


import fr.abes.theses.thesesmisc.entities.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IDocumentDao extends JpaRepository<Document, Integer> {
        Page<Document> findAll(Pageable pageable);

}
