package fr.abes.theses.thesesmisc.service;



import fr.abes.theses.thesesmisc.entities.Document;

import java.util.List;

public interface IDocumentService {
    List<Document> findAll();
    Document findById(Integer id);
}
