package fr.abes.theses.thesesmisc.tasklets.copieprodtotest;

import fr.abes.theses.thesesmisc.model.DocumentProcess;
import fr.abes.theses.thesesmisc.model.Tef;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CopyProdToTestProcessor  implements ItemProcessor<DocumentProcess, DocumentProcess> {

    @Override
    public DocumentProcess process(DocumentProcess documentProcess) throws Exception {

        Tef documentTef = new Tef(documentProcess.document.getDoc());
        String oldId = documentTef.getIdInTEF();

        documentProcess.edited = documentTef.searchAndReplace(
                String.valueOf(documentProcess.document.getIdDoc()),
                oldId,
                String.valueOf(documentProcess.document.getIdDoc()));

        documentProcess.document.setDoc(documentTef.documentTef.asXML());

        return documentProcess;
    }
}
