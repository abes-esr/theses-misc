package fr.abes.theses.thesesmisc.tasklets.syncbddtosolr;

import fr.abes.theses.thesesmisc.model.DocumentProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SyncBddToSolrProcessor implements ItemProcessor<DocumentProcess, DocumentProcess> {
    @Override
    public DocumentProcess process(DocumentProcess documentProcess) throws Exception {
        return documentProcess;
    }
}
