package fr.abes.theses.thesesmisc.tasklets.searchandreplace;

import fr.abes.theses.thesesmisc.entities.Document;
import fr.abes.theses.thesesmisc.model.DocumentProcess;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class SearchAndReplaceReader implements ItemReader<DocumentProcess>, StepExecutionListener {

    private final DocumentService service;
    @Value("${startingPage}")
    private Integer startingPage;
    @Value("${chunkSize}")
    private Integer chunkSize;
    private List<Document> documents;
    private AtomicInteger iPage;
    private AtomicInteger iDocument;

    List<SearchReplace> searchReplaceList;
    private AtomicInteger iIds;

    public SearchAndReplaceReader(DocumentService service) {
        this.iPage = new AtomicInteger();
        this.iDocument = new AtomicInteger();
        this.searchReplaceList = new ArrayList<>();
        this.iIds = new AtomicInteger();
        this.service = service;
    }

    @Override
    public DocumentProcess read() throws Exception {
        if (this.iIds.get() < this.searchReplaceList.size()) {
            SearchReplace searchReplace = this.searchReplaceList.get(this.iIds.getAndIncrement());
            DocumentProcess documentProcess =
                    new DocumentProcess(
                            this.service.getDao().getDocument().findById(Integer.valueOf(searchReplace.id)).orElse(null),
                            searchReplace);
            return documentProcess;
        }
        return null;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        try {
            log.info(this.startingPage.toString());
            this.iPage.set(this.startingPage);
            Reader in;
            try {
               in = new FileReader("src/main/resources/searchReplace.csv");
            } catch (Exception e) {
                in = new FileReader("searchReplace.csv");
            }
            final Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(',').withFirstRecordAsHeader().parse(in);
            for (final CSVRecord record : records) {
                this.searchReplaceList.add(new SearchReplace(record.get("ID"), record.get("search"), record.get("replace")));
            }
        } catch (Exception e) {
            log.error("Erreur dans beforeStep searchReplace");
        }

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
