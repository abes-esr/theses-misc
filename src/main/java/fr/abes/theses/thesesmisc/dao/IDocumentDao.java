package fr.abes.theses.thesesmisc.dao;


import fr.abes.theses.thesesmisc.entities.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IDocumentDao extends JpaRepository<Document, Integer> {
        Page<Document> findAll(Pageable pageable);

        @Query(value = "select d.idDoc from Document d order by d.idDoc desc")
        Page<Integer> findAllById(Pageable pageable);

        @Query(value = "select d from Document d where d.codeEtab = ?1 order by d.idDoc desc")
        Page<Document> findAllByCodeEtab(String codeEtab, Pageable pageable);

        @Procedure(procedureName = "copy_prod_to_test_star")
        void copyProdToTestStar(@Param("p_codeetab")String codeEtab);

        @Procedure(procedureName = "copy_prod_to_test_sujets")
        void copyProdToTestSujets(@Param("p_codeetab")String codeEtab);



        boolean existsById (Integer idDoc);

}
