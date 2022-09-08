package fr.abes.theses.thesesmisc.tasklets.deletewhitespacesbdd;

import fr.abes.theses.thesesmisc.entities.Compte;
import fr.abes.theses.thesesmisc.entities.CompteSTEP;
import fr.abes.theses.thesesmisc.model.DocumentProcess;
import fr.abes.theses.thesesmisc.model.IdToChange;
import fr.abes.theses.thesesmisc.service.impl.DocumentService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class DeleteWhiteSpacesBddReader implements ItemReader<DocumentProcess> {

    @Getter
    private final DocumentService service;

    private AtomicInteger iIds = new AtomicInteger();

    private List<Integer> ids = new ArrayList<>();;

    public DeleteWhiteSpacesBddReader(DocumentService service) throws IOException {
        this.service = service;

        Reader in = new FileReader("src/main/resources/UBFC-STEP.csv");
        CSVFormat fmt = CSVFormat.EXCEL.withDelimiter(';').withFirstRecordAsHeader();
        Iterable<CSVRecord> records = fmt.parse(in);

        for (CSVRecord record : records) {
            ids.add(Integer.parseInt(record.get(0)));
        }
    }

    @Override
    public DocumentProcess read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (iIds.get() < ids.size()) {

            IdToChange idToChange = new IdToChange();;
            idToChange.Id = String.valueOf(ids.get(iIds.getAndIncrement()));
            Optional<CompteSTEP> compte = Optional.ofNullable(service.getDao().getCompteSTEP().getCompteByIdDoc(Integer.valueOf(idToChange.Id)));
            return new DocumentProcess(
                    service.getDao().getDocument().findById(Integer.parseInt(idToChange.Id)).orElse(null),
                    idToChange,
                    compte.orElse(null)
            );
        } else {
            return null;
        }
    }
}
