package fr.abes.theses.thesesmisc.service;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.*;
import org.dom4j.tree.BaseElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class XPathService {

    public static final String STAR_GESTION = "/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion";
    public static final String STAR_GEST_TRTS_SORTIES_SUDOC = STAR_GESTION + "/traitements/sorties/sudoc";
    public static final String STEP_GESTION = "/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion";

    public static final String SUBDIVISION_RAMEAU = "/mets:mets/mets:dmdSec[2]/mets:mdWrap/mets:xmlData/tef:thesisRecord/tef:sujetRameau/tef:vedetteRameauNomCommun/tef:subdivision";
    public static final String SUJET_RAMEAU = "/mets:mets/mets:dmdSec[2]/mets:mdWrap/mets:xmlData/tef:thesisRecord/tef:sujetRameau";

    public static final String ID_SOURCE_STEP = "/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/entree";
    public static final String ID_SOURCE_STAR = "/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/entree";


    public static final String ETAB_DIFFUSEUR = "/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/diffusion/etabDiffuseur";

    public static final String TEF_EDITION = "/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/tef:edition";

    public static final String SORTIES_CINES = "/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/cines";

    public static final String WORKFLOW = "/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow";
    public static final String WORKFLOW_SCOL = "/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/rolesMD/SCOL";
    public static final String WORKFLOW_BIBL = "/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/rolesMD/BIBL";
    public static final String WORKFLOW_FICH = "/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/rolesMD/FICH";
    public static final String WORKFLOW_VALID = "/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/workflow/VALID";
    public static final String STEP_NNT = "/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/sorties/nnt";


    public static final List<String> typeBalises = new ArrayList<>(
            Arrays.asList("tef:vedetteRameauPersonne",
                    "tef:vedetteRameauCollectivite",
                    "tef:vedetteRameauFamille",
                    "tef:vedetteRameauAuteurTitre",
                    "tef:vedetteRameauTitre",
                    "tef:vedetteRameauNomCommun",
                    "tef:vedetteRameauNomGeographique"));

    public static void setAttribut(String xpath, String attribut, String valeur, Document document) {
        try {
            // A précompiler a la création de la class
            XPath path = DocumentHelper.createXPath(xpath);
            List<Node> nodes = path.selectNodes(document);
            Element elem = (Element) nodes.get(0);
            if (elem != null && elem.attribute(attribut) != null) {
                elem.attribute(attribut).setValue(valeur);
            }
        } catch (Exception e) {
            log.error("Erreur générique dans setAttribut pour le xpath : " + xpath + " et attribut : " + attribut
                    + " et valeur : " + valeur, e);
            throw e;
        }
    }

    public static void setValue(String xpath, String value, Document document) {
        try {
            XPath path = DocumentHelper.createXPath(xpath);
            List<Node> nodes = path.selectNodes(document);
            Element elem = (Element) nodes.get(0);
            elem.setText(value);

        } catch (Exception e) {
            log.error("Erreur générique dans setValue pour le xpath : " + xpath
                    + " et valeur : " + value, e);
            throw e;
        }
    }

    public static String getAttribut(String xpath, String attribute, Document document) {
        XPath path = DocumentHelper.createXPath(xpath);
        Element elem = (Element) path.selectNodes(document).get(0);
        return elem.attribute(attribute).getValue();
    }

    public static String getValue(String xpath, Document document) {
        XPath path = DocumentHelper.createXPath(xpath);
        Element elem = (Element) path.selectNodes(document).get(0);
        return elem.getText();
    }

    public static boolean setUrlEtabDiffuseurCas5(Document document, String url) {
        setAttribut(ETAB_DIFFUSEUR, "etabDiffuseurPolEtablissement", "oui", document);
        setValue(ETAB_DIFFUSEUR + "/urlEtabDiffuseur", url, document);

        BaseElement node = new BaseElement("dc:identifier");
        node.addAttribute("xsi:type", "dcterms:URI");
        node.setText(url);
        addElement(TEF_EDITION, node, document);

        return true;
    }

    public static boolean setUrlEtabDiffuseurCas1(Document document, String url) {

        BaseElement node = new BaseElement("dc:identifier");
        node.addAttribute("xsi:type", "tef:URI_intranetEmbargo");
        node.setText(url);

        addElement(TEF_EDITION, node, document);

        return true;
    }

    public static void addElement(String xpath, Node node, Document document) {
        XPath path = DocumentHelper.createXPath(xpath);
        List<Node> nodes = path.selectNodes(document);
        Element elem = (Element) nodes.get(0);
        elem.add(node);
    }

    public static boolean deleteThesEcritAcademique(Document document) {
        boolean edited = false;

        for (String typeBalise : typeBalises) {
            XPath path = buildXPath(typeBalise);

            List<Node> elem = path.selectNodes(document);
            for (Node node : elem) {
                if ("Thèses et écrits académiques".equals(node.getText())) {
                    node.detach();
                    edited = true;
                }
            }
        }

        return edited;
    }

    private static XPath buildXPath(String typeBalise) {
        StringBuilder xPath = new StringBuilder(SUJET_RAMEAU);
        xPath.append("/");
        xPath.append(typeBalise);
        xPath.append("/tef:subdivision");

        return DocumentHelper.createXPath(xPath.toString());
    }

    public static List<Node> deleteAllSubdivisionForme(Document document) {

        List<Node> nodeToReturn = new ArrayList<>();
        for (String typeBalise : typeBalises) {
            XPath path = buildXPath(typeBalise);

            List<Node> elems = path.selectNodes(document);
            for (Node node : elems) {
                if (((Element) node).attribute("type") != null) {
                    if ("subdivisionDeForme".equals(((Element) node).attribute("type").getValue()) && !"Thèses et écrits académiques".equals(node.getText())) {
                        addOneTime(nodeToReturn, node);
                        node.detach();
                    }
                } else {
                    log.error("Not found attribute \"type\"");
                }
            }
        }
        return nodeToReturn;
    }

    private static void addOneTime(List<Node> nodeToReturn, Node node) {
        if (nodeToReturn.isEmpty()) {
            nodeToReturn.add(node);
        } else {
            if (isShouldBeAdded(nodeToReturn, node)) {
                nodeToReturn.add(node);
            }
        }
    }

    private static boolean isShouldBeAdded(List<Node> nodeToReturn, Node node) {
        boolean shouldBeAdded = true;
        for (Node nodeAlreadyintoList : nodeToReturn) {
            if (nodeAlreadyintoList.getText().equals(node.getText())) {
                shouldBeAdded = false;
            }
        }
        return shouldBeAdded;
    }

    public static void addVedetteRameau(Document document, List<Node> nodes) {
        XPath path = DocumentHelper.createXPath(SUJET_RAMEAU);

        Node nodeSujetRameau = path.selectNodes(document).get(0);

        Element e = (Element) nodeSujetRameau;

        for (Node node : nodes) {
            ((Element) node).remove(((Element) node).attribute("type"));
            node.setName("tef:elementdEntree");
            //add node + vedetteRameauGenreForme
            e.addElement("tef:vedetteRameauGenreForme").add(node);
        }
    }

    public static void changeContentId(String idSource, Document documentTef) {
        XPathService.setAttribut(ID_SOURCE_STAR, "idSource", idSource, documentTef);
    }

    public static boolean deleteHistEtCritique(Document documentTef) {
        boolean edited = false;

        for (String typeBalise : typeBalises) {
            XPath path = buildXPath(typeBalise);

            List<Node> elem = path.selectNodes(documentTef);
            for (Node node : elem) {
                if ("Histoire et critique".equals(node.getText())) {
                    node.detach();
                    edited = true;
                }
            }
        }

        return edited;
    }

    public static boolean deleteWhiteSpaceIdSourceStep(Document documentTef) {
        String idSource = XPathService.getAttribut(ID_SOURCE_STEP, "idSource", documentTef).replace(" ", "");
        XPathService.setAttribut(ID_SOURCE_STEP, "idSource", idSource, documentTef);
        return true;
    }
    public static boolean deleteWhiteSpaceIdSourceStar(Document documentTef) {
        String idSource = XPathService.getAttribut(ID_SOURCE_STAR, "idSource", documentTef).replace(" ", "");
        XPathService.setAttribut(ID_SOURCE_STAR, "idSource", idSource, documentTef);
        return true;
    }

    public static boolean addNumeroPACStar(String numeroPAC, Document documentTef) {
        XPathService.setAttribut(SORTIES_CINES, "numeroPAC", numeroPAC, documentTef);
        return true;
    }

    public static boolean addEtatRetourCines(Document documentTef) {
        XPathService.setAttribut(SORTIES_CINES, "indicCines", "OK", documentTef);
        XPathService.setAttribut(SORTIES_CINES, "dateCines", "2022-09-20T00:00:01Z", documentTef);
        XPathService.setAttribut(STAR_GESTION, "etat", "WFpostArchivage", documentTef);
        XPathService.setAttribut(WORKFLOW, "etatWF", "postAtraiter", documentTef);
        XPathService.setAttribut(WORKFLOW_SCOL, "etatMD", "postAtraiter", documentTef);
        XPathService.setAttribut(WORKFLOW_BIBL, "etatMD", "postAtraiter", documentTef);
        XPathService.setAttribut(WORKFLOW_FICH, "etatMD", "postAtraiter", documentTef);
        XPathService.setAttribut(WORKFLOW_VALID, "etatVALID", "archivee", documentTef);
        return false;
    }

    public static boolean addEtatRetourCinesStep(Document documentTef, String nnt) {
        XPathService.setAttribut(STEP_NNT, "dateNnt", "2022-09-20T00:00:01Z", documentTef);
        XPathService.setAttribut(STEP_NNT, "sourceNnt", "star", documentTef);
        XPathService.setAttribut(STEP_NNT, "indicNnt", "OK", documentTef);
        XPathService.setValue(STEP_NNT, nnt, documentTef);

        XPathService.setAttribut(STEP_GESTION, "stepEtat", "these", documentTef);

        return true;
    }
}
