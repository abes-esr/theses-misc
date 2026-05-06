package fr.abes.theses.thesesmisc.dao;


import fr.abes.theses.thesesmisc.entities.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IDocumentDao extends JpaRepository<Document, Integer> {
        Page<Document> findAll(Pageable pageable);

        @Query(value = "select d.idDoc from Document d order by d.idDoc desc")
        Page<Integer> findAllById(Pageable pageable);

        @Query(value = "select d from Document d where d.codeEtab = ?1 order by d.idDoc desc")
        Page<Document> findAllByCodeEtab(String codeEtab, Pageable pageable);

        Page<Document> findAllByIdDocIn(List<Integer> ids, Pageable pageable);



        boolean existsById (Integer idDoc);

}
