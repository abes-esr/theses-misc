package fr.abes.theses.thesesmisc.tasklets.deletedoublonsolr;

import fr.abes.theses.thesesmisc.service.impl.DocumentService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
public class DeleteDoublonSolrWriter implements ItemWriter<String> {

    @Value("${solr.url}")
    private String urlSolr;

    @Getter
    private final DocumentService service;

    public DeleteDoublonSolrWriter(DocumentService service) {
        this.service = service;
    }

    @Override
    public void write(List<? extends String> list) throws Exception {
        String urlSolrUpdate = urlSolr + "/solr1/update";
        for (String id :
                list) {
            StringWriter sw = new StringWriter();
            postData(new StringReader("<delete><id>" + id + "</id></delete>"), sw, urlSolrUpdate);
            if (sw.toString().indexOf("<int name=\"status\">0</int>") < 0) {
                log.error("unexpected response from solr..." + id);
            }
            log.info("Unindexed " + id);
        }
        StringWriter sw = new StringWriter();
        postData(new StringReader("<commit/>"), sw, urlSolrUpdate);

/*        for (String id : list) {
            try {
                if (service.getDao().getDocument().findById(Integer.valueOf(id)).isPresent()) {
                    service.getDao().getDocument().deleteById(Integer.valueOf(id));
                    log.info("Delete bdd: " + id);
                } else {
                    log.info("No doc in bdd : " + id);
                }
            } catch (Exception e) {
                log.warn("Erreur lors de la suppression de l'id : " + id + " dans la bdd");
            }
        }*/
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
/*        for (String id : list) {
            try {
                if (service.getDao().getDocument().findById(Integer.valueOf(id)).isPresent()) {
                    service.getDao().getDocument().deleteById(Integer.valueOf(id));
                } else {
                    log.info("No doc in bdd : " + id);
                }
            } catch (Exception e) {
                log.warn("Erreur lors de la suppression de l'id : " + id + " dans la bdd");
            }
        }

    }
}*/
