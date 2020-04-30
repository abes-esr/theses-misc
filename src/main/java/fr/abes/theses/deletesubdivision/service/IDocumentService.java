package fr.abes.theses.deletesubdivision.service;



import fr.abes.theses.deletesubdivision.entities.Document;

import java.util.List;

public interface IDocumentService {
    List<Document> findAll();
    Document findById(Integer id);
}
