package fr.abes.theses.thesesmisc.tasklets.deletedoublonsolr;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;


@Slf4j
@Component
public class DeleteDoublonSolrProcessor implements ItemProcessor<String, String> {


    @Value("${solr.url}")
    private String urlSolr;

    @Override
    public String process(String id) throws IOException {
        return id;
    }
        /*urlSolr += "/solr2/update";
        StringWriter sw = new StringWriter();
        postData(new StringReader("<delete><id>" + id + "</id></delete>"), sw, urlSolr);
        if (sw.toString().indexOf("<int name=\"status\">0</int>") < 0) {
            log.error("unexpected response from solr...");
        }
        postData(new StringReader("<commit/>"), sw, urlSolr);
        log.info("Id : " + id + " delete from index");
        return id;
    }

    *//**
     * Reads data from the data reader and posts it to solr,
     * writes to the response to output
     * @throws Exception
     *//*
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

    *//**
     * Pipes everything from the reader to the writer via a buffer
     *//*
    private void pipe(Reader reader, Writer writer) throws IOException {
        char[] buf = new char[1024];
        int read = 0;
        while ((read = reader.read(buf)) >= 0) {
            writer.write(buf, 0, read);
        }
        writer.flush();

    }*/
}
