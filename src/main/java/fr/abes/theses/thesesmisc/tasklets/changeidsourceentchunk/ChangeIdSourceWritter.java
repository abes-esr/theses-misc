package fr.abes.theses.thesesmisc.tasklets.changeidsourceentchunk;

import fr.abes.theses.thesesmisc.model.DocumentProcess;
import fr.abes.theses.thesesmisc.service.impl.DocumentService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ChangeIdSourceWritter implements ItemWriter<DocumentProcess> {

    @Getter
    private final DocumentService service;

    public ChangeIdSourceWritter(DocumentService service) {
        this.service = service;
    }

    @Override
    public void write(List<? extends DocumentProcess> list) throws Exception {
        for (DocumentProcess documentProcess : list) {
            if (documentProcess.edited) {
                try {
                    service.getDao().getDocument().save(documentProcess.document);
                    log.info("idDoc edited : " + documentProcess.document.getIdDoc());
                } catch (Exception e) {
                    log.error("Error in writer, doc : " + documentProcess.document.getIdDoc());
                }

                if (documentProcess.compte != null) {
                    try {
                        service.getDao().getCompte().save(documentProcess.compte);
                        log.info("Compte edited : " + documentProcess.compte.getIdCompte());
                    } catch (Exception e) {
                        log.error("Error in writer, Compte IdDoc : " + documentProcess.document.getIdDoc());
                    }
                }
                if (documentProcess.compteSTEP != null) {
                    try {
                        service.getDao().getCompteSTEP().save(documentProcess.compteSTEP);
                        log.info("Compte STEP edited : " + documentProcess.compteSTEP.getIdCompte());
                    } catch (Exception e) {
                        log.error("Error in writer, Compte IdDoc : " + documentProcess.document.getIdDoc());
                    }
                }
                if (documentProcess.compte == null && documentProcess.compteSTEP == null) {
                    log.error("Compte is null for iddoc : " + documentProcess.document.getIdDoc());
                }
            } else {
                log.error("Document not edited, idToChange : " + documentProcess.idToChange.Id);
            }
        }
    }
}
