package fr.abes.theses.thesesmisc.tasklets.majabesdiffuseur;

import fr.abes.theses.thesesmisc.entities.Compte;
import fr.abes.theses.thesesmisc.model.DocumentProcess;
import fr.abes.theses.thesesmisc.model.IdToChange;
import fr.abes.theses.thesesmisc.service.impl.DocumentService;
import fr.abes.theses.thesesmisc.utils.Utils;
import lombok.Getter;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import net.sf.saxon.functions.FunctionLibraryList;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@Slf4j
public class MajAbesDiffuseurReader implements ItemReader<DocumentProcess> {

    public MajAbesDiffuseurReader(DocumentService service) {
        this.service = service;
    }

    AtomicInteger page = new AtomicInteger(0);
    private AtomicInteger iIds = new AtomicInteger(0);


    @Value("${chunkSize}")
    private Integer chunkSize;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Getter
    private final DocumentService service;

    private List<IdToChange> idToChanges = new ArrayList<>();

    @Override
    public DocumentProcess read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (iIds.get() < idToChanges.size()) {

            IdToChange idToChange = idToChanges.get(iIds.getAndIncrement());
            return new DocumentProcess(
                    service.getDao().getDocument().findById(Integer.parseInt(idToChange.id)).orElse(null),
                    idToChange
            );
        } else {
            return null;
        }
    }

    @BeforeChunk
    void beforeChunk() throws IOException, InterruptedException, ParserConfigurationException, SAXException {
        iIds.set(0);

/*        Set<String> ocn = new HashSet<>(Files.readAllLines(Paths.get("src/main/resources/ocn.txt")));
        Set<String> slm = new HashSet<>(Files.readAllLines(Paths.get("src/main/resources/idStarARediffUrlOK.txt")));

        Set<String> onlyInOcn = new HashSet<>(ocn);
        onlyInOcn.removeAll(slm);
        Set<String> onlyInSlm = new HashSet<>(slm);
        onlyInSlm.removeAll(ocn);*/

        List<String> idsSTAR;
        try {
            try {
                idsSTAR = Files.readAllLines(Paths.get("src/main/resources/logMajAbesDiffuseurSTAR_ids.txt"));
            } catch (Exception e) {
                idsSTAR = Files.readAllLines(Paths.get("logMajAbesDiffuseurSTAR_ids.txt"));
            }

            int fromIndex = page.getAndIncrement() * chunkSize;
            int toIndex = Math.min(fromIndex + chunkSize, idsSTAR.size());

            if (fromIndex >= idsSTAR.size()) {
                idToChanges = new ArrayList<>();
            }

            String idStarOr = idsSTAR.subList(fromIndex, toIndex).stream()
                    .collect(Collectors.joining("%20OR%20"));

            String url_solr = Utils.getUrlSolr(datasourceUrl, username) +
                    "/select/?" +
                    "q=id:(" + idStarOr + ")" +
                    "&fl=id,nnt" +
                    "&rows=" + chunkSize;

            readSorl(url_solr);


        } catch (Exception e) {
            log.error("Erreur dans beforeChunk MajAbesDiffuseurReader : " + e);
        }


        //readSorl(null);
    }

    private void readSorl(String SOLR_URL) throws IOException, InterruptedException, ParserConfigurationException, SAXException {

        if (SOLR_URL == null) {
            SOLR_URL =
                    Utils.getUrlSolr(datasourceUrl, username) +
                            "/select/?" +
                            "q=NOT%20SGindicCcsd:sansObjet%20AND%20SGindicCines:OK%20NOT%20SGetabUrl:[%22%22%20TO%20*]%20AND%20SGtypeDif:internet%20AND%20SGabesDiffPolEtab:non" +
                            "&fl=id,nnt" +
                            "&start=" + page.getAndIncrement() * chunkSize +
                            "&rows=" + chunkSize;
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SOLR_URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Parse XML
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(response.body().getBytes()));

        NodeList docNodes = doc.getElementsByTagName("doc");
        idToChanges = new ArrayList<>();

        for (int i = 0; i < docNodes.getLength(); i++) {
            Element docElement = (Element) docNodes.item(i);
            NodeList children = docElement.getElementsByTagName("str");

            String id = null, nnt = null;
            for (int j = 0; j < children.getLength(); j++) {
                Element child = (Element) children.item(j);
                String name = child.getAttribute("name");
                if ("id".equals(name)) {
                    id = child.getTextContent();
                } else if ("nnt".equals(name)) {
                    nnt = child.getTextContent();
                }
            }
            IdToChange idToChange = new IdToChange();
            idToChange.id = id;
            idToChange.nnt = nnt;

            idToChanges.add(idToChange);
        }
    }
}