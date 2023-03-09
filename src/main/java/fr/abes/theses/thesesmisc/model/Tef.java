package fr.abes.theses.thesesmisc.model;


import fr.abes.theses.thesesmisc.service.XPathService;
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

    public boolean deleteWhiteSpaceIdSourceStep() throws InstantiationException, DocumentException, IOException {
        checkDocumenTef();
        boolean edited = XPathService.deleteWhiteSpaceIdSourceStep(documentTef);

        if (edited){
            deleteCariageReturn();
        }
        return edited;
    }
    public boolean deleteWhiteSpaceIdSourceStar() throws InstantiationException, DocumentException, IOException {
        checkDocumenTef();
        boolean edited = XPathService.deleteWhiteSpaceIdSourceStar(documentTef);

        if (edited){
            deleteCariageReturn();
        }
        return edited;
    }

    public boolean deleteTheseEcritAcademique() throws InstantiationException, IOException, DocumentException {
        checkDocumenTef();
        boolean edited = XPathService.deleteThesEcritAcademique(documentTef);

        if (edited){
            deleteCariageReturn();
        }
        return edited;
    }

    public boolean deleteHistEtCritique() throws DocumentException, IOException, InstantiationException {
        checkDocumenTef();
        boolean edited = XPathService.deleteHistEtCritique(documentTef);

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

    public boolean changeUrlCas5(String url) throws DocumentException, IOException, InstantiationException {
        checkDocumenTef();
        boolean edited = XPathService.setUrlEtabDiffuseurCas5(documentTef, url);

        if (edited){
            deleteCariageReturn();
        }
        return edited;
    }

    public boolean changeUrlCas1(String url) throws DocumentException, IOException, InstantiationException {
        checkDocumenTef();
        boolean edited = XPathService.setUrlEtabDiffuseurCas1(documentTef, url);

        if (edited){
            deleteCariageReturn();
        }
        return edited;

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

    public boolean retourCinesSTAR(String numeroPAC) {
        XPathService.addNumeroPACStar(numeroPAC, documentTef);
        XPathService.addEtatRetourCines(documentTef);
        return true;
    }

    public boolean retourCinesSTEP(String nnt) {
        XPathService.addEtatRetourCinesStep(documentTef, nnt);
        return true;
    }

    public boolean changeIdSourceStar(String odlIdSource, String newIdSource) {
        XPathService.changeIdSourceStar(documentTef, odlIdSource, newIdSource);
        return true;
    }
    public boolean changeIdSourceStep(String odlIdSource, String newIdSource) {
        XPathService.changeIdSourceStep(documentTef, odlIdSource, newIdSource);
        return true;
    }
}
