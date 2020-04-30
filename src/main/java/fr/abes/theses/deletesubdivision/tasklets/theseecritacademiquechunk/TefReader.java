package fr.abes.theses.deletesubdivision.tasklets.theseecritacademiquechunk;

import fr.abes.theses.deletesubdivision.entities.Document;
import fr.abes.theses.deletesubdivision.model.DocumentProcess;
import fr.abes.theses.deletesubdivision.service.impl.DocumentService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class TefReader implements ItemReader<DocumentProcess> {

    @Getter
    private final DocumentService service;

    private List<Document> documents;

    private AtomicInteger iPage = new AtomicInteger();

    private AtomicInteger iDocument = new AtomicInteger();

/*    private AtomicInteger iIds = new AtomicInteger();

    private List<Integer> ids = new ArrayList<>(Arrays.asList(37489));*/

    public TefReader(DocumentService service) {
        this.service = service;
    }

    @Override
    public DocumentProcess read() {
/*        if (iIds.get() < ids.size()){
            return new DocumentProcess(service.getDao().getDocument().findById(ids.get(iIds.getAndIncrement())).orElse(null));
        }
        return null;*/

        if (iDocument.get() < documents.size()){
            return new DocumentProcess(documents.get(iDocument.getAndIncrement()));
        }
        return null;
    }


    @BeforeChunk
    public void beforeChunk(ChunkContext context) {
        PageRequest pageable = PageRequest.of(iPage.getAndIncrement(), 10, Sort.by("idDoc"));
        Page<Document> documentPage = service.getDao().getDocument().findAllDocumentWithPagination(pageable);
        log.info("Reader : Page " + (iPage.get()-1) + " on " + documentPage.getTotalPages());
        documents = documentPage.getContent();
    }

    @AfterChunk
    public void afterChunk(ChunkContext context) {
        iDocument.set(0);
    }
}
