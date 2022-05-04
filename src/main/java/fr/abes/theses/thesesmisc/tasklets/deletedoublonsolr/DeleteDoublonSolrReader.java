package fr.abes.theses.thesesmisc.tasklets.deletedoublonsolr;

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
public class DeleteDoublonSolrReader implements ItemReader<String> {

    private List<String> idToDeletes = new ArrayList<>();

    private AtomicInteger iIds = new AtomicInteger();


    public DeleteDoublonSolrReader() throws IOException {

        Reader in = new FileReader("src/main/resources/DoublonIdsToDelete.csv");
        CSVFormat fmt = CSVFormat.EXCEL.withDelimiter(',');
        Iterable<CSVRecord> records = fmt.parse(in);

        for (CSVRecord record : records) {

            List<String> ligne = new ArrayList<>();
            for (int i = 0; i < record.size(); i++) {
                ligne.add(record.get(i));
            }

            // On supprime le plus recent de la liste
            ligne.sort((o1, o2) -> (int) (Long.parseLong(o1) - Long.parseLong(o2)));
            ligne.remove(ligne.size() - 1);

            idToDeletes.addAll(ligne);
        }
    }

    @Override
    public String read() {
        if (iIds.get() < idToDeletes.size()){
            return idToDeletes.get(iIds.getAndIncrement());
        } else {
            return null;
        }
    }
}
