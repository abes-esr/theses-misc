package fr.abes.theses.deletesubdivision.tasklets.theseecritacademiquechunk;

import fr.abes.theses.deletesubdivision.entities.Document;
import fr.abes.theses.deletesubdivision.model.DocumentProcess;
import fr.abes.theses.deletesubdivision.model.IdStepToChange;
import fr.abes.theses.deletesubdivision.service.impl.DocumentService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class TefReader implements ItemReader<DocumentProcess>, StepExecutionListener {

    @Getter
    private final DocumentService service;

    @Value("${startingPage}")
    private Integer startingPage;

    @Value("${chunkSize}")
    private Integer chunkSize;

    private List<Document> documents;

    private AtomicInteger iPage = new AtomicInteger();

    private AtomicInteger iDocument = new AtomicInteger();

    @Value("#{'${thesesToTreat}'.split(',')}")
    private List<Integer> ids;

    private List<IdStepToChange> idStepToChanges = new ArrayList<>();


    private AtomicInteger iIds = new AtomicInteger();


    public TefReader(DocumentService service) throws IOException {
        this.service = service;

        Reader in = new FileReader("src/main/resources/UNIPdoc.csv");
        CSVFormat fmt = CSVFormat.EXCEL.withDelimiter(';').withFirstRecordAsHeader();
        Iterable<CSVRecord> records = fmt.parse(in);

        for (CSVRecord record : records) {
            IdStepToChange idStepToChange = new IdStepToChange();

            idStepToChange.Idstep = record.get("Idstep");
            idStepToChange.oldCodeInd = record.get("OLD_COD_IND");
            idStepToChange.etab = record.get("ETAB");
            idStepToChange.codeInd = record.get("COD_IND");

            idStepToChanges.add(idStepToChange);
        }
    }


    @Override
    public DocumentProcess read() {

        if (iIds.get() < idStepToChanges.size()) {
            IdStepToChange idStepToChange = idStepToChanges.get(iIds.getAndIncrement());
            return new DocumentProcess(
                    service.getDao().getDocument().findById(Integer.parseInt(idStepToChange.Idstep)).orElse(null),
                    idStepToChange
            );
        } else {
            return null;
        }

/*            if (iIds.get() < ids.size()){
                log.info("Reader : id " + ids.get(iIds.getAndIncrement()).toString());
                return new DocumentProcess(service.getDao().getDocument().findById(ids.get(iIds.getAndIncrement())).orElse(null));
            }
            return null;*/

/*        if (iDocument.get() < documents.size()) {
            return new DocumentProcess(documents.get(iDocument.getAndIncrement()));
        }
        return null;*/
    }


    @BeforeChunk
    public void beforeChunk(ChunkContext context) {
/*        PageRequest pageable = PageRequest.of(iPage.getAndIncrement(), chunkSize, Sort.by("idDoc"));
        Page<Document> documentPage = service.getDao().getDocument().findAllDocumentWithPagination(pageable);
        log.info("Reader : Page " + (iPage.get() - 1) + " on " + documentPage.getTotalPages());
        documents = documentPage.getContent();*/
    }

    @AfterChunk
    public void afterChunk(ChunkContext context) {
        iDocument.set(0);
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("startingPage : " + startingPage.toString());
        iPage.set(startingPage.intValue());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
