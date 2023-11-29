package fr.abes.theses.thesesmisc.tasklets.histoireetcritique;

import fr.abes.theses.thesesmisc.model.DocumentProcess;
import fr.abes.theses.thesesmisc.model.IdToChange;
import fr.abes.theses.thesesmisc.service.impl.DocumentService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class HistoireEtCritiqueReader implements ItemReader<DocumentProcess> {

    @Getter
    private final DocumentService service;

    private AtomicInteger iIds = new AtomicInteger();

    private List<Integer> ids = new ArrayList<>();
    ;

    public HistoireEtCritiqueReader(DocumentService service) throws IOException {
        this.service = service;
        try {
            Reader in = new FileReader("src/main/resources/hist_critique.csv");
            CSVFormat fmt = CSVFormat.EXCEL.withDelimiter(',').withFirstRecordAsHeader();
            Iterable<CSVRecord> records = fmt.parse(in);

            for (CSVRecord record : records) {
                ids.add(Integer.parseInt(record.get(0)));
            }
        } catch (Exception e) {
            log.error("Erreur dans le constructeur" + e);
        }
    }

    @Override
    public DocumentProcess read() {
        if (iIds.get() < ids.size()) {

            IdToChange idToChange = new IdToChange();
            ;
            idToChange.id = String.valueOf(ids.get(iIds.getAndIncrement()));
            return new DocumentProcess(
                    service.getDao().getDocument().findById(Integer.parseInt(idToChange.id)).orElse(null),
                    idToChange
            );
        } else {
            return null;
        }
    }
}
