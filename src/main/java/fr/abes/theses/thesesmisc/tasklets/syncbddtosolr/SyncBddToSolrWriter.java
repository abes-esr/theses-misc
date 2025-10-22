package fr.abes.theses.thesesmisc.tasklets.syncbddtosolr;

import fr.abes.theses.thesesmisc.model.DocumentProcess;
import fr.abes.theses.thesesmisc.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
public class SyncBddToSolrWriter implements ItemWriter<DocumentProcess> {

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Override
    public void write(List<? extends DocumentProcess> list) throws Exception {

        for (DocumentProcess doc : list) {
            if (doc.document != null) {
                if (username.contains("STAR")) {
                    log.info("iddocSTAR : " + doc.document.getIdDoc());
                    Utils.indexerDansSolr(
                            doc.document.getIdDoc(),
                            doc.document.getDoc(),
                            "src/main/resources/xls/tef2solr.xsl",
                            Utils.getUrlSolr(datasourceUrl, username) + "/update"
                    );

                } else {
                    log.info("iddocSTEP : " + doc.document.getIdDoc());
                    Utils.indexerDansSolr(
                            doc.document.getIdDoc(),
                            doc.document.getDoc(),
                            "src/main/resources/xls/sujets2solr.xsl",
                            Utils.getUrlSolr(datasourceUrl, username) + "/update"
                    );
                }
            } else {
                log.warn("iddoc not found : " + doc.idToChange.id);
            }

        }


    }



}
