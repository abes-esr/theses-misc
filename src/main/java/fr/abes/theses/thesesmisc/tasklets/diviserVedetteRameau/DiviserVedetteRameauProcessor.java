package fr.abes.theses.thesesmisc.tasklets.diviserVedetteRameau;

import fr.abes.theses.thesesmisc.model.DocumentProcess;
import fr.abes.theses.thesesmisc.model.Tef;
import fr.abes.theses.thesesmisc.utils.ScissionRameauEntry;
import fr.abes.theses.thesesmisc.utils.ScissionRameauList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Prend un tef et vérifie pour chaque ligne de ScissionRameauList si le sujet Rameau concerné par une scission est présent
 * Et le remplace par les deux sujets de sa subdivision
 */
@Slf4j
@Component
public class DiviserVedetteRameauProcessor implements ItemProcessor<DocumentProcess, DocumentProcess> {
    @Override
    public DocumentProcess process(DocumentProcess documentProcess) throws Exception {
        // Récupérer la liste des scissions rameau
        List<ScissionRameauEntry> scissionRameauEntries = ScissionRameauList.getEntries();
        Tef documentTef = new Tef(documentProcess.document.getDoc());
        try {
            for (ScissionRameauEntry entry : scissionRameauEntries) {
                documentProcess.edited = documentProcess.edited || documentTef.searchAndReplaceScissionRameau(entry); // edited reste à true meme si les prochaines entrées n'apparaissent pas dans le tef
            }
        } catch (Exception e) {
            log.info("Error in SubdivisionDeFormeProcessor, doc : " + documentProcess.document.getIdDoc());
        }

        if (documentProcess.edited) {
            log.debug("Document TEF modifié: {}", documentProcess.document.getIdDoc());
        }
        documentProcess.document.setDoc(documentTef.documentTef.asXML());

        return documentProcess;
    }
}
