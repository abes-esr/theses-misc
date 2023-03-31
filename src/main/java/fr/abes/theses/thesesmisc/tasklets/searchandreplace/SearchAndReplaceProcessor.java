package fr.abes.theses.thesesmisc.tasklets.searchandreplace;

import fr.abes.theses.thesesmisc.model.DocumentProcess;
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
            final Pattern pattern = Pattern.compile(documentProcess.searchReplace.search, Pattern.LITERAL);
            final Matcher matcher = pattern.matcher(documentProcess.document.getDoc());
            if (matcher.find()) {
                documentProcess.document.setDoc(matcher.replaceAll(documentProcess.searchReplace.replace));
                documentProcess.edited = true;
            }
            else {
                log.info("SearchAndReplaceProcessor no search found, idDoc : " + documentProcess.document.getIdDoc());
            }
        }
        return documentProcess;
    }
}
