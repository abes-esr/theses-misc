package fr.abes.theses.thesesmisc.tasklets.syncbddtosolr;

import fr.abes.theses.thesesmisc.model.DocumentProcess;
import fr.abes.theses.thesesmisc.service.impl.DocumentService;
import fr.abes.theses.thesesmisc.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class SyncBddToSolrReader implements ItemReader<DocumentProcess> {

    private final DocumentService service;
    private List<Integer> iddocList = new ArrayList<>();
    private AtomicInteger iIds = new AtomicInteger();
    private AtomicInteger iPage = new AtomicInteger();

    @Value("${chunkSize}")
    private Integer chunkSize;

    @Value("${startingPage}")
    private Integer startingPage;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.url}")
    private String databaseUrl;

    private String urlSolr;

    public SyncBddToSolrReader(DocumentService service) throws IOException {

        this.service = service;
    }



    @BeforeChunk
    public void beforeChunck() throws IOException {

        this.urlSolr = Utils.getUrlSolr(databaseUrl, username);

        beforeChuck1();
        beforeChunk2();
       // beforeChunk3();
    }
    public void beforeChuck1() {

        iPage.set(startingPage);

        log.info("Pour tout id dans BDD " + username
                + ", s'il ne sont pas dans le solr, on indexe dans le solr " + username);

        while (iPage.get() * chunkSize * 100 < 300000) {

            log.info("iPage : " + iPage.get());

            PageRequest pageable = PageRequest.of(iPage.getAndIncrement(), chunkSize * 100, Sort.by("idDoc").descending());

            service.getDao().getDocument().findAllById(pageable).toList().stream()
                    .forEach(id -> {
                        try {
                            if (!Utils.isInSolr(id, this.urlSolr)) {
                                log.info("indexation solr : " + id + " dans " + username);
                                boolean res = Utils.indexerDansSolr(id,
                                        this.service.getDao().getDocument().findById(id).orElseThrow().getDoc(),
                                        Utils.getXslSolr(username),
                                        this.urlSolr);
                                if (!res) {
                                    log.error("pas indexer " + id);
                                }
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }

    public void beforeChunk2() throws IOException {

        iPage.set(startingPage);

        log.info("Pour tout id dans solr " + username
                + ", s'il ne sont pas en BDD on supprime dans le solr " + username);

        while (iPage.get() * chunkSize * 100 < 300000) {

            log.info("iPage : " + iPage.get());

            PageRequest pageable = PageRequest.of(iPage.getAndIncrement(), chunkSize * 100, Sort.by("id").ascending());

            Utils.findAllSolr("SGcodeEtab:UNIP", this.urlSolr, pageable).parallelStream()  // all : *:*
                    .forEach(id -> {
                        try {
                            if (!service.getDao().getDocument().existsById(id)) {
                                log.info("Suppression solr : " + id + " dans " + username);
                                Utils.supprimerDeSolr(id,
                                        this.urlSolr);
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }


    public void beforeChunk3() throws IOException {

        iPage.set(startingPage);

        log.info("Pour tout id dans solr " + username
                + ", s'il n'a pas de code ETAB, on l'indexe dans le solr " + username);

        while (iPage.get() * chunkSize * 100 < 1000) {

            log.info("iPage : " + iPage.get());

            PageRequest pageable = PageRequest.of(iPage.getAndIncrement(), chunkSize * 100, Sort.by("id").ascending());

            Utils.findAllSolr("-SGcodeEtab:[\"\" TO *]", this.urlSolr, pageable)
                    .forEach(id -> {
                        try {
                            if (service.getDao().getDocument().existsById(id)) {
                                log.info("Indexation solr : " + id + " dans " + username);
                                Utils.indexerDansSolr(id,
                                        this.service.getDao().getDocument().findById(id).orElseThrow().getDoc(),
                                        Utils.getXslSolr(username),
                                        this.urlSolr);
                            } else {
                                log.info("not in bdd " + id + " " + username);
                                Utils.supprimerDeSolr(id, this.urlSolr);
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }

    @Override
    public DocumentProcess read() throws Exception {
        return null;
    }
}
