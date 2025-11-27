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
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

        if (edited) {
            deleteCariageReturn();
        }
        return edited;
    }

    public boolean searchAndReplace(String id, String search, String replace) throws InstantiationException, DocumentException, IOException {
        checkDocumenTef();

        boolean edited = false;

        final Pattern pattern = Pattern.compile(search, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(documentTef.asXML());
        if (matcher.find()) {
            documentTef = DocumentHelper.parseText(matcher.replaceAll(replace));
            edited = true;
        } else {
            log.info("SearchAndReplaceProcessor no search found, idDoc : " + id);
        }

        if (edited) {
            deleteCariageReturn();
        }
        return edited;
    }

    public boolean deleteWhiteSpaceIdSourceStar() throws InstantiationException, DocumentException, IOException {
        checkDocumenTef();
        boolean edited = XPathService.deleteWhiteSpaceIdSourceStar(documentTef);

        if (edited) {
            deleteCariageReturn();
        }
        return edited;
    }

    public boolean deleteTheseEcritAcademique() throws InstantiationException, IOException, DocumentException {
        checkDocumenTef();
        boolean edited = XPathService.deleteThesEcritAcademique(documentTef);

        if (edited) {
            deleteCariageReturn();
        }
        return edited;
    }

    public boolean deleteHistEtCritique() throws DocumentException, IOException, InstantiationException {
        checkDocumenTef();
        boolean edited = XPathService.deleteHistEtCritique(documentTef);

        if (edited) {
            deleteCariageReturn();
        }
        return edited;
    }

    public boolean deleteSubdivisionDeForme() throws IOException, DocumentException {
        List<Node> nodes = XPathService.deleteAllSubdivisionForme(documentTef);
        if (!nodes.isEmpty()) {
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

        if (edited) {
            deleteCariageReturn();
        }
        return edited;
    }

    public boolean changeUrlCas1(String url) throws DocumentException, IOException, InstantiationException {
        checkDocumenTef();
        boolean edited = XPathService.setUrlEtabDiffuseurCas1(documentTef, url);

        if (edited) {
            deleteCariageReturn();
        }
        return edited;

    }

    private void deleteCariageReturn() throws IOException, DocumentException {
        StringWriter sw = new StringWriter();
        XMLWriter writer = new XMLWriter(sw, OutputFormat.createPrettyPrint());
        writer.write(documentTef);

        documentTef = DocumentHelper.parseText(sw.toString());
    }

    public boolean setDateAbandon(LocalDate dateAbandon, String etat) {
        XPathService.setDateAbandon(dateAbandon, documentTef);
        XPathService.setStepEtat(etat, documentTef);
        return true;
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

    public boolean majAbesDiffuseurOui(String urlAbesDiffuseur) throws Exception {

        String cas = XPathService.getAttribut("/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements", "scenario", documentTef);

        switch (cas) {
            case "cas1" :
                String xpath1 = "//mets:dmdSec[ends-with(@ID, 'VERSION_COMPLETE.DESCRIPTION.EDITION_ARCHIVAGE')]//tef:edition";
                XPathService.majAbesDiffuseurAjoutUriCasN(urlAbesDiffuseur, xpath1, documentTef);
                break;
            case "cas2" :
                String xpath2 = "//mets:dmdSec[ends-with(@ID, 'VERSION_COMPLETE.DESCRIPTION.EDITION_1')]//tef:edition";
                XPathService.majAbesDiffuseurAjoutUriCasN(urlAbesDiffuseur, xpath2, documentTef);
                break;
            case "cas3" :
                String xpath3 = "//mets:dmdSec[ends-with(@ID, 'VERSION_INCOMPLETE_1.DESCRIPTION.EDITION_1')]//tef:edition";
                XPathService.majAbesDiffuseurAjoutUriCasN(urlAbesDiffuseur, xpath3, documentTef);
                break;
            case "cas4" :
                String xpath4 = "//mets:dmdSec[ends-with(@ID, 'VERSION_INCOMPLETE_1.DESCRIPTION.EDITION_1')]//tef:edition";
                XPathService.majAbesDiffuseurAjoutUriCasN(urlAbesDiffuseur, xpath4, documentTef);
                break;
            default :
                throw new Exception("Cas non valide");
        }

        XPathService.majAbesDiffuseurOui(urlAbesDiffuseur, documentTef);
        return true;
    }

    public String getIdInTEF() {
        String id = XPathService.getAttribut(XPathService.METS_DMDSEC, "ID", documentTef);
        Pattern pattern = Pattern.compile("_(\\d+)\\.");
        Matcher matcher = pattern.matcher(id);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }

    }

}
