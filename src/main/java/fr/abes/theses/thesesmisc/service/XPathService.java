package fr.abes.theses.thesesmisc.service;

import fr.abes.theses.thesesmisc.utils.ScissionRameauEntry;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.*;
import org.dom4j.tree.BaseElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class XPathService {

    public static final String METS_HDR = "/mets:mets/mets:metsHdr";

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
    public static final String STEP_DATE_ABANDON = "/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/suj:vie/suj:dateAbandon";
    public static final String STEP_SUJ_VIE = "/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/suj:vie";

    public static final String STAR_ETAB_DIFFUSEUR = "/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/diffusion/abesDiffuseur";
    //                                               "/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/diffusion/abesDiffuseur/@abesDiffuseurPolEtablissement


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
        if (path.selectNodes(document).size() == 0) {
            return null;
        }
        Element elem = (Element) path.selectNodes(document).get(0);
        return elem.attribute(attribute).getValue();
    }

    public static String getValue(String xpath, Document document) {
        XPath path = DocumentHelper.createXPath(xpath);
        if (path.selectNodes(document).size() == 0) {
            return null;
        }
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

    public static boolean changeIdSourceStar(Document documentTef, String odlIdSource, String newIdSource) {
        String idSource = XPathService.getAttribut(ID_SOURCE_STAR, "idSource", documentTef).replace(odlIdSource, newIdSource);
        XPathService.setAttribut(ID_SOURCE_STAR, "idSource", idSource, documentTef);
        return true;
    }

    public static boolean changeIdSourceStep(Document documentTef, String odlIdSource, String newIdSource) {
        String idSource = XPathService.getAttribut(ID_SOURCE_STEP, "idSource", documentTef).replace(odlIdSource, newIdSource);
        XPathService.setAttribut(ID_SOURCE_STEP, "idSource", idSource, documentTef);
        return true;
    }

    public static boolean setStepEtat(String etat, Document documentTef) {
        XPathService.setAttribut(STEP_GESTION, "stepEtat", etat, documentTef);
        return true;
    }

    public static boolean setDateAbandon(LocalDate dateAbandon, Document documentTef) {

        XPath path = DocumentHelper.createXPath(STEP_DATE_ABANDON);
        List<Node> elem = path.selectNodes(documentTef);

        for (Node node : elem) {
            node.setText(dateAbandon.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            return true;
        }

        BaseElement node = new BaseElement("suj:dateAbandon");
        node.setText(dateAbandon.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        XPathService.addElement(STEP_SUJ_VIE, node, documentTef);

        return true;
    }

    public static boolean majAbesDiffuseurOui(String urlAbesDiffuseur, Document documentTef) {
        XPathService.setAttribut(STAR_ETAB_DIFFUSEUR, "abesDiffuseurPolEtablissement", "oui", documentTef);
        XPathService.setAttribut(STAR_ETAB_DIFFUSEUR, "urlAbesDiffuseur", urlAbesDiffuseur, documentTef);
        return true;
    }

    public static boolean majAbesDiffuseurAjoutUriCasN(String urlAbesDiffuseur, String xpath, Document documentTef) {

        // Récupérer les noeuds correspondant au xpath
        List<Node> nodes = documentTef.selectNodes(xpath);

        // Vérifier si l'URL existe déjà
        for (Node node : ((Element) documentTef.selectNodes(xpath).get(0)).elements()) {
            String urlExistante = node.getText();
            if (urlAbesDiffuseur.equals(urlExistante)) {
                // L'URL est déjà présente, on ne l'ajoute pas
                log.info("Url déjà présente : " + urlAbesDiffuseur);
                return false;
            }
        }


        BaseElement node = new BaseElement("dc:identifier");
        node.addAttribute("xsi:type", "dcterms:URI");
        node.setText(urlAbesDiffuseur);

        addElement(xpath, node, documentTef);

        return true;
    }


    /**
     * Récupère tous les sous-nœuds des balises données par le xpath
     * dont l'attribut attribut a une valeur attributeValue.
     *
     * @param documentTef Le tef
     * @param attributeValue Le nom de l'attribut à rechercher
     * @param attributeValue La valeur de l'attribut à rechercher
     * @param xpath Le xpath pour sélectionner les noeuds
     * @return Une liste de nœuds correspondant aux critères.
     */
    public static List<Node> getNodesByParentNodeNameAndAttributeValue(Document documentTef, String attribut, String attributeValue, String xpath) {
        List<Node> resultNodes = new ArrayList<>();

        XPath xpathVedette = DocumentHelper.createXPath(xpath);
//        xpathVedette.setNamespaceURIs(Map.of("tef", "http://namespace-uri"));

        List<Node> vedetteNodes = xpathVedette.selectNodes(documentTef);

        // Pour chaque balise, chercher ses sous-nœuds
        for (Node vedetteNode : vedetteNodes) {
            if (vedetteNode instanceof Element) {
                Element vedetteElement = (Element) vedetteNode;
                XPath xpathSubNodes = DocumentHelper.createXPath(
                        "./*[@" + attribut + "='" + attributeValue + "']"
                );

                // Sélectionner les sous-nœuds correspondants
                List<Node> subNodes = xpathSubNodes.selectNodes(vedetteElement);
                resultNodes.addAll(subNodes);
            }
        }

        return resultNodes;
    }

    public static boolean replaceVedettesRameau(List<Node> vedetteNodes, ScissionRameauEntry entry) {
        boolean isModified = false;
        for (Node node: vedetteNodes) {
            try {
                Node newNode1;
                Node newNode2;
                if (node.getName().equals("tef:elementdEntree")) {
                    // Cas 1
                    newNode1 = createNewElementWithEntry("tef:elementdEntree", entry.getNewPpn1(), entry.getNewLabel1());
                    newNode2 = createNewElementWithEntry("tef:subdivision", entry.getNewPpn2(), entry.getNewLabel2());
                } else if (node.getName().equals("tef:subdivision")) {
                    // Cas 2
                    newNode1 = createNewElementWithEntry("tef:subdivision", entry.getNewPpn1(), entry.getNewLabel1());
                    newNode2 = createNewElementWithEntry("tef:subdivision", entry.getNewPpn2(), entry.getNewLabel2());
                } else {
                    throw new Exception("Pas d'élement <tef:elementdEntree> ou <tef:subdivision> trouvé pour l'autorité rameau " + entry.getOldPpn());
                }

                replaceNodeWithTwoNewNodes(node, newNode1, newNode2);
                isModified = true;
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        return isModified;
    }

    /**
     * Crée un nouveau noeud de type spécifié avec les valeurs de ScissionRameauEntry.
     *
     * @param nodeType Le type du nouveau nœud (ex: "tef:elementdEntree").
     * @param newPpn La nouvelle valeur PPN.
     * @param newLabel La nouvelle valeur du libellé.
     * @return Le nouveau nœud créé.
     */
    private static Node createNewElementWithEntry(String nodeType, String newPpn, String newLabel) {
        Element newElement = DocumentHelper.createElement(nodeType);

        newElement.addAttribute("autoriteExterne", newPpn);
        newElement.addAttribute("autoriteSource", "Sudoc");
        newElement.setText(newLabel);

        if ("tef:subdivision".equals(nodeType)) {
            newElement.addAttribute("type", "subdivisionDeSujet");
        }

        return newElement;
    }

    /**
     * Remplace un nœud par deux nouveaux noeuds au même endroit et au même niveau.
     * @param nodeToReplace Le noeud à supprimer.
     * @param newNode1 Le premier nouveau noeud.
     * @param newNode2 Le deuxième nouveau noeud.
     */
    public static void replaceNodeWithTwoNewNodes(Node nodeToReplace, Node newNode1, Node newNode2) {
        Element parent = nodeToReplace.getParent();

        if (parent == null) {
            throw new IllegalArgumentException("Le noeud à remplacer n'a pas de parent.");
        }

        int index = parent.indexOf(nodeToReplace);

        nodeToReplace.detach();
        parent.content().add(index, newNode1);
        parent.content().add(index + 1, newNode2);
    }
}
