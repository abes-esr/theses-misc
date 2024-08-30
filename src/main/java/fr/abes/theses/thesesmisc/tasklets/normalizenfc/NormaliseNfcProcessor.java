package fr.abes.theses.thesesmisc.tasklets.normalizenfc;

import fr.abes.theses.thesesmisc.model.DocumentProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.text.Normalizer;

@Component
@Slf4j
public class NormaliseNfcProcessor implements ItemProcessor<DocumentProcess, DocumentProcess> {
    @Override
    public DocumentProcess process(DocumentProcess documentProcess) throws Exception {
        documentProcess.document.setDoc(
                Normalizer.normalize(documentProcess.document.getDoc(), Normalizer.Form.NFC)
        );
        documentProcess.edited = true;
        return documentProcess;
    }
}
