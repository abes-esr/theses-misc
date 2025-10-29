package fr.abes.theses.thesesmisc.tasklets.copieprodtotest;

import fr.abes.theses.thesesmisc.entities.Document;
import fr.abes.theses.thesesmisc.model.DocumentProcess;
import fr.abes.theses.thesesmisc.service.impl.DocumentService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class CopyProdToTestReader implements ItemReader<DocumentProcess> {
    @Getter
    private final DocumentService service;

    private List<Document> documents = new ArrayList<>();

    @Value("${chunkSize}")
    private Integer chunkSize;

    @Value("${codeEtab}")
    private String codeEtab;

    private AtomicInteger iPage = new AtomicInteger();

    private AtomicInteger iDocument = new AtomicInteger();
    public CopyProdToTestReader(DocumentService service) {
        this.service = service;
    }

    @BeforeChunk
    public void beforeChunk(ChunkContext context) {
        PageRequest pageable = PageRequest.of(iPage.getAndIncrement(), chunkSize, Sort.by("idDoc").descending());
        Page<Document> documentPage = service.getDao().getDocument().findAllByCodeEtab(codeEtab, pageable);
        log.info("Reader : Page " + (iPage.get() - 1) + " / " + documentPage.getTotalPages());
        documents = documentPage.getContent();
    }

    @Override
    public DocumentProcess read() throws Exception {

        if (iDocument.get() < documents.size()) {
            return new DocumentProcess(documents.get(iDocument.getAndIncrement()));
        }


        return null;
    }

    @AfterChunk
    public void afterChunk(ChunkContext context) {
        iDocument.set(0);
    }

}
