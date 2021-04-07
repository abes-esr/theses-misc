package fr.abes.theses.deletesubdivision.tasklets.changeURLchunk;

import fr.abes.theses.deletesubdivision.model.DocumentProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
@Component
public class ChangeURLProcessor implements ItemProcessor<DocumentProcess, DocumentProcess> {
    @Override
    public DocumentProcess process(DocumentProcess documentProcess) {

        if (documentProcess.document != null) {

            Pattern pattern = Pattern.compile(documentProcess.urlToChange.getAncienne_url(), Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(documentProcess.document.getDoc());

            if (matcher.find()) {
                documentProcess.document.setDoc(
                        documentProcess.document.getDoc().replaceAll(documentProcess.urlToChange.getAncienne_url(), documentProcess.urlToChange.getNouvelle_url()));
                documentProcess.edited = true;
            } else {
                log.info("Url non trouvé : idDoc : " + documentProcess.document.getIdDoc());
            }
        }

        return documentProcess;
    }
}
