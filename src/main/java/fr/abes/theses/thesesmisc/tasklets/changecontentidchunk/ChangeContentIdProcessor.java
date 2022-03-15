package fr.abes.theses.thesesmisc.tasklets.changecontentidchunk;

import fr.abes.theses.thesesmisc.model.DocumentProcess;
import fr.abes.theses.thesesmisc.model.Tef;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChangeContentIdProcessor implements ItemProcessor<DocumentProcess, DocumentProcess> {
    @Override
    public DocumentProcess process(DocumentProcess documentProcess) {

        if (documentProcess.document != null && documentProcess.compte != null) {
            try {
                if (documentProcess.idToChange.codeInd != null) {

                    String idSource = documentProcess.idToChange.etab + "_0755976N_" + documentProcess.idToChange.codeInd;

                    Tef documentTef = new Tef(documentProcess.document.getDoc());
                    documentProcess.edited = documentTef.changeContentId(idSource);
                    documentProcess.document.setDoc(documentTef.documentTef.asXML());

                    if (documentProcess.compte.getLogin() != null) {
                        documentProcess.compte.setLogin(idSource);
                        documentProcess.compte.setMdp(idSource);
                        documentProcess.compte.setNUMIDENT("0755976N_" + documentProcess.idToChange.codeInd);
                    }
                } else {
                    log.error("Error in processor, doc : " + documentProcess.document.getIdDoc() + "new codeInd not found");
                }

            } catch (Exception e) {
                log.error("Error in processor, doc : " + documentProcess.document.getIdDoc());
            }
        } else {
            log.error("Doc or Compte " + documentProcess.idToChange.Id + " not found");
        }

        return documentProcess;
    }
}
