package fr.abes.theses.thesesmisc.tasklets.majabesdiffuseur;

import fr.abes.theses.thesesmisc.model.DocumentProcess;
import fr.abes.theses.thesesmisc.model.Tef;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MajAbesDiffuseurProcessor implements ItemProcessor<DocumentProcess, DocumentProcess> {

    @Value("${spring.datasource.url}")
    private String datasourceUrl;


    @Override
    public DocumentProcess process(DocumentProcess documentProcess) throws Exception {
        try {
            Tef documentTef = new Tef(documentProcess.document.getDoc());
            documentProcess.edited = documentTef.majAbesDiffuseurOui(
                    getUrlTheses(datasourceUrl) + documentProcess.idToChange.nnt + "/abes");
            documentProcess.document.setDoc(documentTef.documentTef.asXML());
        } catch (Exception e){
            log.info("Error in processor, doc : " + documentProcess.document.getIdDoc());
        }

        return documentProcess;
    }

    private String getUrlTheses(String datasourceUrl) {
        if (datasourceUrl.contains("-p-")) {
            return "https://theses.fr/";
        } else if (datasourceUrl.contains("-t-")) {
            return "https://v2-test.theses.fr/";
        } else {
            throw new IllegalArgumentException("Datasource url is not correct");
        }
    }
}
