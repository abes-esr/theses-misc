package fr.abes.theses.thesesmisc.tasklets.copieprodtotest;

import fr.abes.theses.thesesmisc.model.DocumentProcess;
import fr.abes.theses.thesesmisc.model.Tef;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Slf4j
public class CopyProdToTestProcessor implements ItemProcessor<DocumentProcess, DocumentProcess> {

    @Value("${copyProdToTest.fileAppli}")
    private boolean copyFileAppli;

    String starStock = "/applis/portail/theses/STARSTOCK/";

    @Override
    public DocumentProcess process(DocumentProcess documentProcess) throws Exception {

        Tef documentTef = new Tef(documentProcess.document.getDoc());
        String oldId = documentTef.getIdInTEF();

        documentProcess.edited = documentTef.searchAndReplace(
                String.valueOf(documentProcess.document.getIdDoc()),
                oldId,
                String.valueOf(documentProcess.document.getIdDoc()));

        documentProcess.document.setDoc(documentTef.documentTef.asXML());

        if (copyFileAppli) {
            renameFolderInApplis(oldId, String.valueOf(documentProcess.document.getIdDoc()), documentProcess.document.getCodeEtab());
        }

        return documentProcess;
    }

    private boolean renameFolderInApplis(String oldId, String newId, String codeEtab) {
        String oldPath = starStock + codeEtab + "/THESE_" + oldId;
        String newPath = starStock + codeEtab + "/THESE_" + newId;

        File oldFile = new File(oldPath);
        File newFile = new File(newPath);

        if (!oldFile.exists()) {
            log.error("Le fichier " + oldFile.getAbsolutePath() + " n'existe pas.");
            return false;
        }

        return oldFile.renameTo(newFile);
    }
}
