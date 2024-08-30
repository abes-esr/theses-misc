package fr.abes.theses.thesesmisc.tasklets.deletedoublonsolr;

import fr.abes.theses.thesesmisc.entities.Document;
import fr.abes.theses.thesesmisc.service.XPathService;
import fr.abes.theses.thesesmisc.service.impl.DocumentService;
import fr.abes.theses.thesesmisc.utils.Utils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.dom4j.io.SAXReader;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class DeleteDoublonSolrWriter implements ItemWriter<String> {

    @Getter
    private final DocumentService service;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${databaseDelete}")
    private Boolean databaseDelete = false;

    @Value("${deleteIfNoCinesBlock}")
    private Boolean deleteIfNoCinesBlock = false;

    public DeleteDoublonSolrWriter(DocumentService service) {
        this.service = service;
    }

    public static org.dom4j.Document parseStringToDOM(String xmlString) throws Exception {
        SAXReader reader = new SAXReader();
        return reader.read(new StringReader(xmlString));
    }

    @Override
    public void write(List<? extends String> list) throws Exception {

        String urlSolr = Utils.getUrlSolr(username);
        String urlSolrUpdate = urlSolr + "/update";

        for (String id : list) {

            deleteInSolr(urlSolrUpdate, id);

            Optional<Document> document = service.getDao().getDocument().findById(Integer.valueOf(id));

            if (document.isPresent()) {
                org.dom4j.Document doc = parseStringToDOM(document.get().getDoc());

                String indicCines = XPathService.getAttribut("/mets:mets/mets:dmdSec/mets:mdWrap/mets:xmlData/star_gestion/traitements/sorties/cines",
                        "indicCines",
                        doc);

                if ("OK".equals(indicCines)) {
                    log.info("indicCines OK, pas de suppression dans la base : " + id);
                } else {
                    if (indicCines == null && deleteIfNoCinesBlock) {
                        log.info("Pas de block Cines et suppression de la base : " + id);
                        deleteInBdd(id);
                    }
                    if (indicCines != null) {
                        log.info("Delete bdd: " + id);
                        deleteInBdd(id);
                    }
                }
            }
        }
        StringWriter sw = new StringWriter();
        postData(new StringReader("<commit/>"), sw, urlSolrUpdate);


    }

    private void deleteInBdd(String id) {
        try {
            if (databaseDelete) {
                service.getDao().getDocument().deleteById(Integer.valueOf(id));
            }
        } catch (Exception e) {
            log.warn("Erreur lors de la suppression de l'id : " + id + " dans la bdd");
        }
    }


    private void deleteInSolr(String urlSolrUpdate, String id) throws IOException {
        StringWriter sw = new StringWriter();
        postData(new StringReader("<delete><id>" + id + "</id></delete>"), sw, urlSolrUpdate);
        if (sw.toString().indexOf("<int name=\"status\">0</int>") < 0) {
            log.error("unexpected response from solr..." + id);
        }
        log.info("Unindexed " + id);
    }

    /**
     * Reads data from the data reader and posts it to solr,
     * writes to the response to output
     *
     * @throws Exception
     */
    public void postData(Reader data, Writer output, String url) throws IOException {
        URL solrUrl = new URL(url);
        HttpURLConnection urlc = null;
        try {
            urlc = (HttpURLConnection) solrUrl.openConnection();
            try {
                urlc.setRequestMethod("POST");
            } catch (ProtocolException e) {
                throw new ProtocolException("Shouldn't happen: HttpURLConnection doesn't support POST??");
            }
            urlc.setDoOutput(true);
            urlc.setDoInput(true);
            urlc.setUseCaches(false);
            urlc.setAllowUserInteraction(false);
            urlc.setRequestProperty("Content-type", "text/xml; charset=UTF-8");

            try (OutputStream out = urlc.getOutputStream()) {
                Writer writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
                pipe(data, writer);
                writer.close();
            } catch (IOException e) {
                throw new IOException("IOException while posting data", e);
            }

            try (InputStream in = urlc.getInputStream()) {
                Reader reader = new InputStreamReader(in);
                pipe(reader, output);
                reader.close();
            } catch (IOException e) {
                throw new IOException("IOException while reading response", e);
            }

        } catch (IOException e) {
            try {
                assert urlc != null;
                log.info("Solr returned an error: " + urlc.getResponseMessage());
                throw new IOException("Erreur lors du post sur solr : "
                        + urlc.getResponseMessage(), e);
            } catch (IOException f) {
                log.info("Connection error (is Solr running at " + solrUrl + " ?): " + e);
                throw new IOException("Erreur de connexion à solr", e);
            }
        } finally {
            if (urlc != null) {
                urlc.disconnect();
            }
        }
    }

    /**
     * Pipes everything from the reader to the writer via a buffer
     */
    private void pipe(Reader reader, Writer writer) throws IOException {
        char[] buf = new char[1024];
        int read = 0;
        while ((read = reader.read(buf)) >= 0) {
            writer.write(buf, 0, read);
        }
        writer.flush();

    }
}
