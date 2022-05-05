package fr.abes.theses.thesesmisc.model;

import fr.abes.theses.thesesmisc.entities.Compte;
import fr.abes.theses.thesesmisc.entities.Document;

public class DocumentProcess {
    public Document document;
    public boolean edited = false;
    public IdToChange idToChange;

    public String url;

    public Compte compte;

    public DocumentProcess(Document document, IdToChange idToChange) {
        this.document = document;
        this.idToChange = idToChange;
    }

    public DocumentProcess(Document document){
        this.document = document;
    }

    public DocumentProcess(Document document, IdToChange idToChange, Compte compte){
        this.document = document;
        this.idToChange = idToChange;
        this.compte = compte;
    }

    public DocumentProcess(Document document, IdToChange idToChange, String url){
        this.document = document;
        this.idToChange = idToChange;
        this.url = url;
    }
}
