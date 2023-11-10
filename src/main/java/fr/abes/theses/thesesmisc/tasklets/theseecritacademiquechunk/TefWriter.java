package fr.abes.theses.thesesmisc.tasklets.theseecritacademiquechunk;

import fr.abes.theses.thesesmisc.model.DocumentProcess;
import fr.abes.theses.thesesmisc.service.impl.DocumentService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TefWriter implements ItemWriter<DocumentProcess> {

    @Getter
    private final DocumentService service;

    public TefWriter(DocumentService service) {
        this.service = service;
    }

    @Override
    public void write(List<? extends DocumentProcess> list) {
        for (DocumentProcess documentProcess : list) {
            if (documentProcess.edited) {
                try {
                    service.getDao().getDocument().save(documentProcess.document);
                    log.info("idDoc edited : " + documentProcess.document.getIdDoc());
                } catch (Exception e) {
                    log.error("Error in writer, doc : " + documentProcess.document.getIdDoc());
                }
            } else {
                if (documentProcess.document != null) {
                    log.info("Compte not edited IdDoc : " + documentProcess.document.getIdDoc());
                } else {
                    log.warn("Le document est null, vérifier l'environnement STAR/SUJETS");
                }
            }

 /*               if (documentProcess.compte != null) {
                    try {
                        service.getDao().getCompte().save(documentProcess.compte);
                        log.info("Compte edited : " + documentProcess.compte.getIdCompte());
                    } catch (Exception e) {
                        log.error("Error in writer, Compte IdDoc : " + documentProcess.document.getIdDoc());
                    }
                } else {
                    log.error("Compte not edited IdDoc : " + documentProcess.document.getIdDoc());
                }*/
        }
    }
}
