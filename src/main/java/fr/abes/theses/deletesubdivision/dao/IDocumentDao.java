package fr.abes.theses.deletesubdivision.dao;


import fr.abes.theses.deletesubdivision.entities.Document;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IDocumentDao extends JpaRepository<Document, Integer> {

/*    @Query("select IDDOC ,DOC ,TEXTE ,CODEETAB ,ENVOISOLR  from " +
            "(select rownum rnum, a from document a where rownum <= 10)" +
            "where rnum > 1;")
    List<Document> getDocumentsPagination(@Param("rowMin") rowMin, @Param("rowMax") rowMax)*/

    @Query("SELECT doc FROM Document doc ORDER BY doc.idDoc")
    Page<Document> findAllDocumentWithPagination(Pageable pageable);
}
