package fr.abes.theses.thesesmisc.tasklets.checkIndexBdd;

import fr.abes.theses.thesesmisc.entities.Document;
import fr.abes.theses.thesesmisc.model.BddData;
import fr.abes.theses.thesesmisc.model.Tef;
import fr.abes.theses.thesesmisc.service.XPathService;
import fr.abes.theses.thesesmisc.service.impl.DocumentService;
import lombok.Getter;
import lombok.extern.java.Log;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Log
@Component
public class CheckIndexBddReader implements ItemReader<BddData> {

    private AtomicInteger bddPage = new AtomicInteger();
    @Getter
    private final DocumentService service;
    @Value("${chunkSize}")
    private Integer chunkSize;

    private List<Document> documents;

    private AtomicInteger iDocument = new AtomicInteger();

    private static final String NOM = "/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:auteur/tef:nom";
    private static final String PRENOM = "/mets:mets/mets:amdSec/mets:techMD/mets:mdWrap/mets:xmlData/tef:thesisAdmin/tef:auteur/tef:prenom";


    public CheckIndexBddReader(DocumentService service) {
        this.service = service;
    }

    @Override
    public BddData read() throws Exception {

        Document document = documents.get(iDocument.getAndIncrement());

        if (document != null) {
            Tef tef = new Tef(document.getDoc());
            log.info("ID " + document.getId());
            return new BddData(
                    document.getId(),
                    XPathService.getValue(NOM, tef.documentTef),
                    XPathService.getValue(PRENOM, tef.documentTef)
            );
        } else {
            return null;
        }
    }

    @BeforeChunk
    public void beforeChunk(ChunkContext context) {
       PageRequest pageable = PageRequest.of(bddPage.getAndIncrement(), chunkSize, Sort.by("idDoc"));
        Page<Document> documentPage = service.getDao().getDocument().findAllDocumentWithPagination(pageable);
        log.info("Reader : Page " + (bddPage.get() - 1) + " on " + documentPage.getTotalPages());
        documents = documentPage.getContent();
    }

    @AfterChunk
    public void afterChunk(ChunkContext context) {
        iDocument.set(0);
    }
}
