package fr.abes.theses.deletesubdivision.tasklets.theseecritacademiquechunk;

import fr.abes.theses.deletesubdivision.model.DocumentProcess;
import fr.abes.theses.deletesubdivision.model.Tef;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TefProcessor implements ItemProcessor<DocumentProcess, DocumentProcess> {
    @Override
    public DocumentProcess process(DocumentProcess documentProcess) throws Exception {

        try {
            Tef documentTef = new Tef(documentProcess.document.getDoc());
            documentProcess.edited = documentTef.deleteTheseEcritAcademique();
            documentProcess.document.setDoc(documentTef.documentTef.asXML());
        } catch (Exception e){
            log.info("Error in processor, doc : " + documentProcess.document.getIdDoc());
        }

        return documentProcess;
    }
}
