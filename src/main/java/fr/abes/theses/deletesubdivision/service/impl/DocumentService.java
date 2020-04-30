package fr.abes.theses.deletesubdivision.service.impl;


import fr.abes.theses.deletesubdivision.dao.impl.DaoProvider;
import fr.abes.theses.deletesubdivision.entities.Document;
import fr.abes.theses.deletesubdivision.service.IDocumentService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService implements IDocumentService {

    @Autowired
    @Getter
    private DaoProvider dao;

    @Override
    public List<Document> findAll() {
        return getDao().getDocument().findAll();
    }

    @Override
    public Document findById(Integer id) {
        return getDao().getDocument().findById(id).orElse(null);
    }
}
