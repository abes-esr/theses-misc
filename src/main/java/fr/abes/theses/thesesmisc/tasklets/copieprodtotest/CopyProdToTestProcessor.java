package fr.abes.theses.thesesmisc.tasklets.copieprodtotest;

import fr.abes.theses.thesesmisc.model.DocumentProcess;
import fr.abes.theses.thesesmisc.model.Tef;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Slf4j
public class CopyProdToTestProcessor  implements ItemProcessor<DocumentProcess, DocumentProcess> {

    String starStock = "/applis/portail/theses/STARSTOCK/";

    @Override
    public DocumentProcess process(DocumentProcess documentProcess) throws Exception {

        Tef documentTef = new Tef(documentProcess.document.getDoc());
        String oldId = documentTef.getIdInTEF();

        if (renameFolderInApplis(oldId, String.valueOf(documentProcess.document.getIdDoc()), documentProcess.document.getCodeEtab())) {
            documentProcess.edited = documentTef.searchAndReplace(
                    String.valueOf(documentProcess.document.getIdDoc()),
                    oldId,
                    String.valueOf(documentProcess.document.getIdDoc()));

            documentProcess.document.setDoc(documentTef.documentTef.asXML());
        } else {
            log.info("Iddoc non traité : {} oldIddoc: {}", documentProcess.document.getIdDoc(), oldId);
        }



        return documentProcess;
    }

    private boolean renameFolderInApplis(String oldId, String newId, String codeEtab) {
        String oldPath = starStock + codeEtab + oldId;
        String newPath = starStock + codeEtab + newId;

        File oldFile = new File(oldPath);
        File newFile = new File(newPath);

        if (!oldFile.exists()) {
            log.error("Le fichier " + oldFile.getAbsolutePath() + " n'existe pas.");
            return false;
        }

        return oldFile.renameTo(newFile);
    }
}
