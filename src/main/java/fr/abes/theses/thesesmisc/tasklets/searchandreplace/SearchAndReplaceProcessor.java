package fr.abes.theses.thesesmisc.tasklets.searchandreplace;

import fr.abes.theses.thesesmisc.model.DocumentProcess;
import fr.abes.theses.thesesmisc.model.Tef;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class SearchAndReplaceProcessor implements ItemProcessor<DocumentProcess, DocumentProcess> {
    @Override
    public DocumentProcess process(DocumentProcess documentProcess) throws Exception {
        if (documentProcess.document != null) {

            Tef documentTef = new Tef(documentProcess.document.getDoc());

            documentProcess.edited = documentTef.searchAndReplace(
                    documentProcess.searchReplace.id,
                    documentProcess.searchReplace.search,
                    documentProcess.searchReplace.replace);

            documentProcess.document.setDoc(documentTef.documentTef.asXML());
        }
        return documentProcess;
    }
}
