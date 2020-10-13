package fr.abes.theses.deletesubdivision.model;

import fr.abes.theses.deletesubdivision.entities.Compte;
import fr.abes.theses.deletesubdivision.entities.Document;

public class DocumentProcess {
    public Document document;
    public boolean edited = false;
    public IdToChange idToChange;

    public Compte compte;

    public DocumentProcess(Document document){
        this.document = document;
    }

    public DocumentProcess(Document document, IdToChange idToChange, Compte compte){
        this.document = document;
        this.idToChange = idToChange;
        this.compte = compte;
    }
}
