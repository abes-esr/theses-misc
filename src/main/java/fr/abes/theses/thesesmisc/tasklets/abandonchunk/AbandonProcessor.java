package fr.abes.theses.thesesmisc.tasklets.abandonchunk;

import fr.abes.theses.thesesmisc.model.DocumentProcess;
import fr.abes.theses.thesesmisc.model.Tef;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AbandonProcessor implements ItemProcessor<DocumentProcess, DocumentProcess> {
    @Override
    public DocumentProcess process(DocumentProcess documentProcess) throws Exception {
        if (documentProcess.document != null) {
            try {
                if (documentProcess.idToChange.id != null) {

                    Tef documentTef = new Tef(documentProcess.document.getDoc());
                    documentProcess.edited = documentTef.setDateAbandon(documentProcess.idToChange.date, "abandon");
                    documentProcess.document.setDoc(documentTef.documentTef.asXML());

                } else {
                    log.error("Error in processor, doc : " + documentProcess.document.getIdDoc() + "IDDOC not found");
                }

            } catch (Exception e) {
                log.error("Error in processor, doc : " + documentProcess.document.getIdDoc());
            }
        } else {
            log.error("Doc " + documentProcess.idToChange.id + " not found");
        }

        return documentProcess;
    }
}

