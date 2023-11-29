package fr.abes.theses.thesesmisc.tasklets.changeUrl;

import fr.abes.theses.thesesmisc.model.DocumentProcess;
import fr.abes.theses.thesesmisc.model.IdToChange;
import fr.abes.theses.thesesmisc.service.impl.DocumentService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
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
public class ChangeUrlCas5Reader implements ItemReader<DocumentProcess>, StepExecutionListener {

    @Getter
    private final DocumentService service;

    private List<IdToChange> idToChanges = new ArrayList<>();

    private AtomicInteger iIds = new AtomicInteger();

    public ChangeUrlCas5Reader(DocumentService service) throws IOException {
        this.service = service;
        try {
            Reader in = new FileReader("src/main/resources/CAS5ajout.csv");
            CSVFormat fmt = CSVFormat.EXCEL.withDelimiter(',').withFirstRecordAsHeader();
            Iterable<CSVRecord> records = fmt.parse(in);

            for (CSVRecord record : records) {
                IdToChange idToChange = new IdToChange();

                idToChange.id = record.get("ID");
                idToChange.url = record.get("URL");

                idToChanges.add(idToChange);
            }
        } catch (Exception e) {
            log.error("Erreur dans le constructeur" + e);
        }
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {

    }

    @Override
    public DocumentProcess read() {

        if (iIds.get() < idToChanges.size()) {

            IdToChange idToChange = idToChanges.get(iIds.getAndIncrement());
            return new DocumentProcess(
                    service.getDao().getDocument().findById(Integer.parseInt(idToChange.id)).orElse(null),
                    idToChange,
                    idToChange.url
            );
        } else {
            return null;
        }
    }


    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
