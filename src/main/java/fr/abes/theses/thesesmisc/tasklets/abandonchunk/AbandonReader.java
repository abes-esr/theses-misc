package fr.abes.theses.thesesmisc.tasklets.abandonchunk;

import fr.abes.theses.thesesmisc.model.DocumentProcess;
import fr.abes.theses.thesesmisc.model.IdToChange;
import fr.abes.theses.thesesmisc.model.SearchReplace;
import fr.abes.theses.thesesmisc.service.impl.DocumentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.Reader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class AbandonReader implements ItemReader<DocumentProcess>, StepExecutionListener {

    private final DocumentService service;
    List<IdToChange> abandonList = new ArrayList<>();
    private AtomicInteger iIds = new AtomicInteger();

    public AbandonReader(DocumentService service) {
        this.service = service;
    }

    @Override
    public DocumentProcess read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (this.iIds.get() < this.abandonList.size()) {
            IdToChange abandon = this.abandonList.get(this.iIds.getAndIncrement());
            return new DocumentProcess(
                    this.service.getDao().getDocument().findById(Integer.valueOf(abandon.id)).orElse(null),
                    abandon);
        }
        return null;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        try {
            Reader in;
            try {
                in = new FileReader("src/main/resources/abandon.csv");
            } catch (Exception e) {
                in = new FileReader("abandon.csv");
            }

            final Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(',').withFirstRecordAsHeader().parse(in);
            for (final CSVRecord record : records) {
                IdToChange abandon = new IdToChange();
                abandon.id = record.get("ID STEP");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                abandon.date = LocalDate.parse(record.get("date"), formatter);
                this.abandonList.add(abandon);
            }
        } catch (Exception e) {
            log.error("Erreur dans beforeStep AbandonReader");
        }

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
