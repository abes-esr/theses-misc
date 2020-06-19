package fr.abes.theses.deletesubdivision.model;

import fr.abes.theses.deletesubdivision.entities.Document;

public class DocumentProcess {
    public Document document;
    public boolean edited = false;
    public IdStepToChange idStepToChange;

    public DocumentProcess(Document document){
        this.document = document;
    }

    public DocumentProcess(Document document, IdStepToChange idStepToChange){
        this.document = document;
        this.idStepToChange = idStepToChange;
    }
}
