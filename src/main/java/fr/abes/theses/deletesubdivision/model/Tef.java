package fr.abes.theses.deletesubdivision.model;


import fr.abes.theses.deletesubdivision.service.XPathService;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;


@Slf4j
public class Tef {

    private static final String ERROR_MSG_EMPTY_TEF = "Erreur : le champ documentTef dans MajDonneesGestionTef est null.";

    public Document documentTef = null;


    public Tef(String document) throws DocumentException, InstantiationException {
        documentTef = DocumentHelper.parseText(document);
        checkDocumenTef();
    }

    private void checkDocumenTef() throws InstantiationException {
        if (documentTef == null) {
            log.error(ERROR_MSG_EMPTY_TEF);
            throw new InstantiationException("DocumentTef n'est pas initalisé");
        }
    }

    public boolean deleteTheseEcritAcademique() throws InstantiationException, IOException, DocumentException {
        checkDocumenTef();
        boolean edited = XPathService.deleteThesEcritAcademique(documentTef);

        if (edited){
            deleteCariageReturn();
        }
        return edited;
    }

    public boolean deleteSubdivisionDeForme() throws IOException, DocumentException {
        List<Node> nodes = XPathService.deleteAllSubdivisionForme(documentTef);
        if (!nodes.isEmpty()){
            XPathService.addVedetteRameau(documentTef, nodes);
            deleteCariageReturn();
            return true;
        } else {
            return false;
        }

    }

    private void deleteCariageReturn() throws IOException, DocumentException {
        StringWriter sw = new StringWriter();
        XMLWriter writer = new XMLWriter(sw, OutputFormat.createPrettyPrint( ));
        writer.write(documentTef);

        documentTef = DocumentHelper.parseText(sw.toString());
    }

    public boolean changeContentId(String idSource) {
        XPathService.changeContentId(idSource, documentTef);
        return true;
    }
}
