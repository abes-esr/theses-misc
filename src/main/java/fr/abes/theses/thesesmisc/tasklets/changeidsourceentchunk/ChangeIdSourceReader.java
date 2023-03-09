package fr.abes.theses.thesesmisc.tasklets.changeidsourceentchunk;

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
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class ChangeIdSourceReader implements ItemReader<DocumentProcess> {

    private List<IdToChange> idToChanges = new ArrayList<>();

    private AtomicInteger iIds = new AtomicInteger();

    @Getter
    private final DocumentService service;

    public ChangeIdSourceReader(DocumentService service) throws IOException {
        this.service = service;

        Reader in = new FileReader("src/main/resources/ChangeIdSourceSTAR.csv");
        //Reader in = new FileReader("src/main/resources/ChangeIdSourceSTEP.csv");
        CSVFormat fmt = CSVFormat.EXCEL.withDelimiter(',');
        Iterable<CSVRecord> records = fmt.parse(in);

        for (CSVRecord record : records) {
            IdToChange idToChange = new IdToChange();
            idToChange.Id = record.get(0);
            idToChanges.add(idToChange);
        }
    }

    @Override
    public DocumentProcess read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (iIds.get() < idToChanges.size()){

            IdToChange idToChange = idToChanges.get(iIds.getAndIncrement());
            Compte compte = null;
            CompteSTEP compteSTEP = null;
            try {
                compte = service.getDao().getCompte().getCompteByIdDoc(Integer.valueOf(idToChange.Id));
                //compteSTEP = service.getDao().getCompteSTEP().getCompteByIdDoc(Integer.valueOf(idToChange.Id));
            } catch (Exception e) {
                log.error("Unable to get Compte objet from table, IdDoc : " + idToChange.Id);
                return new DocumentProcess(
                        service.getDao().getDocument().findById(Integer.parseInt(idToChange.Id)).orElse(null),
                        idToChange);
            }
            return new DocumentProcess(
                    service.getDao().getDocument().findById(Integer.parseInt(idToChange.Id)).orElse(null),
                    idToChange,
                    compte
                    //compteSTEP
            );
        } else {
            return null;
        }
    }
}
