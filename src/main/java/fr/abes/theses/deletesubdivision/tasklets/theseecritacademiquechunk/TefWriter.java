package fr.abes.theses.deletesubdivision.tasklets.theseecritacademiquechunk;

import fr.abes.theses.deletesubdivision.model.DocumentProcess;
import fr.abes.theses.deletesubdivision.service.impl.DocumentService;
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
    public void write(List<? extends DocumentProcess> list) throws Exception {
        for (DocumentProcess documentProcess : list) {
            if (documentProcess.edited) {
                log.info("idDoc edited : " + documentProcess.document.getIdDoc());
                try {
                    //service.getDao().getDocument().save(documentProcess.document);
                } catch (Exception e) {
                    log.info("Error in writer, doc : " + documentProcess.document.getIdDoc());
                }
            }
        }
    }
}
