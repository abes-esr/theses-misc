package fr.abes.theses.deletesubdivision.service;

import fr.abes.theses.deletesubdivision.model.IdStepToChange;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class XPathService {

    public static final String XPATH_STAR_GESTION = "/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion";
    public static final String XPATH_STAR_GEST_TRTS_SORTIES_SUDOC = XPATH_STAR_GESTION + "/traitements/sorties/sudoc";

    public static final String SUBDIVISION_RAMEAU = "/mets:mets/mets:dmdSec[2]/mets:mdWrap/mets:xmlData/tef:thesisRecord/tef:sujetRameau/tef:vedetteRameauNomCommun/tef:subdivision";
    public static final String SUJET_RAMEAU = "/mets:mets/mets:dmdSec[2]/mets:mdWrap/mets:xmlData/tef:thesisRecord/tef:sujetRameau";

    public static final String ID_SOURCE = "/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/step_gestion/traitements/entree";

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

    public static String getAttribut(String xpath, String attribut, Document document) {
        XPath path = DocumentHelper.createXPath(xpath);
        Element elem = (Element) path.selectNodes(document).get(0);
        return elem.attribute(attribut).getValue();
    }

    public static String getValue(String xpath, Document document) {
        XPath path = DocumentHelper.createXPath(xpath);
        Element elem = (Element) path.selectNodes(document).get(0);
        return elem.getText();
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

    public static void changeContentId(IdStepToChange idStepToChange, Document documentTef) {
        String idSource = idStepToChange.etab + "_0755976N_" + idStepToChange.codeInd;
        XPathService.setAttribut(ID_SOURCE, "idSource", idSource, documentTef);
    }
}
