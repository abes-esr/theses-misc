package fr.abes.theses.thesesmisc.tasklets.deletewhitespacesbdd;

import fr.abes.theses.thesesmisc.model.DocumentProcess;
import fr.abes.theses.thesesmisc.model.Tef;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeleteWhiteSpacesBddProcessor implements ItemProcessor<DocumentProcess, DocumentProcess> {
    @Override
    public DocumentProcess process(DocumentProcess documentProcess) throws Exception {

        //Modif du login
        documentProcess.compteSTEP.setLogin(documentProcess.compteSTEP.getLogin().replace(" ", ""));

        //Modif dans le tef
        try {
            Tef documentTef = new Tef(documentProcess.document.getDoc());
            documentProcess.edited = documentTef.deleteWhiteSpaceIdSourceStep();
            documentProcess.document.setDoc(documentTef.documentTef.asXML());
        } catch (Exception e){
            log.info("Error in processor, doc : " + documentProcess.idToChange.Id);
        }

        return documentProcess;

    }
}
