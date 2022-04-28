package fr.abes.theses.thesesmisc.tasklets.changeUrl;

import fr.abes.theses.thesesmisc.model.DocumentProcess;
import fr.abes.theses.thesesmisc.model.Tef;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChangeUrlCas5Processor implements ItemProcessor<DocumentProcess, DocumentProcess> {
    @Override
    public DocumentProcess process(DocumentProcess documentProcess) throws Exception {
        try {
            Tef documentTef = new Tef(documentProcess.document.getDoc());
            documentProcess.edited = documentTef.changeUrlCas5(documentProcess.url);
            documentProcess.document.setDoc(documentTef.documentTef.asXML());
        } catch (Exception e){
            log.info("Error in processor, doc : " + documentProcess.document.getIdDoc());
        }

        return documentProcess;
    }
}
