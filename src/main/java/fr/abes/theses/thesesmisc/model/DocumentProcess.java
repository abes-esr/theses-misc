package fr.abes.theses.thesesmisc.model;

import fr.abes.theses.thesesmisc.entities.Compte;
import fr.abes.theses.thesesmisc.entities.CompteSTEP;
import fr.abes.theses.thesesmisc.entities.Document;

public class DocumentProcess {
    public Document document;
    public boolean edited = false;
    public IdToChange idToChange;

    public String url;

    public Compte compte;
    public CompteSTEP compteSTEP;
    public SearchReplace searchReplace;

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

    public DocumentProcess(Document document, IdToChange idToChange, CompteSTEP compteSTEP) {
        this.document = document;
        this.idToChange = idToChange;
        this.compteSTEP = compteSTEP;
    }

    public DocumentProcess(Document document, SearchReplace searchReplace) {
        this.document = document;
        this.searchReplace = searchReplace;
    }
}
