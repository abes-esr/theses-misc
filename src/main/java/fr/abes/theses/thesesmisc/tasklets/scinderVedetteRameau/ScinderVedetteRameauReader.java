package fr.abes.theses.thesesmisc.tasklets.scinderVedetteRameau;

import fr.abes.theses.thesesmisc.entities.Document;
import fr.abes.theses.thesesmisc.model.DocumentProcess;
import fr.abes.theses.thesesmisc.service.impl.DocumentService;
import fr.abes.theses.thesesmisc.utils.ScissionRameauList;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class ScinderVedetteRameauReader implements ItemReader<DocumentProcess> {

    private static final int ORACLE_IN_LIMIT = 1000;

    @Getter
    private final DocumentService service;
    private List<Integer> scissionRameauTefIds = ScissionRameauList.getTefIds();

    private List<Document> documents = new ArrayList<>();

    @Value("${chunkSize}")
    private Integer chunkSize;

    private AtomicInteger iPage = new AtomicInteger();

    private AtomicInteger iDocument = new AtomicInteger();
    public ScinderVedetteRameauReader(DocumentService service) {
        this.service = service;
    }

    @BeforeChunk
    public void beforeChunk(ChunkContext context) {
        int effectiveChunkSize = Math.min(chunkSize, ORACLE_IN_LIMIT);
        int startIndex = iPage.getAndIncrement() * effectiveChunkSize;

        if (startIndex >= scissionRameauTefIds.size()) {
            documents = new ArrayList<>();
            return;
        }

        int endIndex = Math.min(startIndex + effectiveChunkSize, scissionRameauTefIds.size());
        List<Integer> tefIdsChunk = scissionRameauTefIds.subList(startIndex, endIndex);

        PageRequest pageable = PageRequest.of(0, effectiveChunkSize, Sort.by("idDoc").descending());

        documents = service.getDao().getDocument()
                .findAllByIdDocIn(tefIdsChunk, pageable)
                .getContent();

        log.info(
                "Reader : IDs {} à {} / {}, documents trouvés : {}",
                startIndex + 1,
                endIndex,
                scissionRameauTefIds.size(),
                documents.size()
        );
    }

    @Override
    public DocumentProcess read() throws Exception {
        if (iDocument.get() < documents.size()) {
            log.info(String.valueOf(documents.get(iDocument.get()).getIdDoc()));
            return new DocumentProcess(documents.get(iDocument.getAndIncrement()));
        }
        return null;
    }

    @AfterChunk
    public void afterChunk(ChunkContext context) {
        iDocument.set(0);
    }
}
