package fr.abes.theses.deletesubdivision.dao.impl;


import fr.abes.theses.deletesubdivision.dao.IDocumentDao;
import lombok.Getter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Getter
@Service
public class DaoProvider {
    @Resource
    private IDocumentDao document;
}
