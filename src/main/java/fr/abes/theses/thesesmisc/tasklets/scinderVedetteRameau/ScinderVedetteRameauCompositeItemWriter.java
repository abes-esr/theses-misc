package fr.abes.theses.thesesmisc.tasklets.scinderVedetteRameau;

import fr.abes.theses.thesesmisc.model.DocumentProcess;
import fr.abes.theses.thesesmisc.tasklets.csv.CsvStatusWriter;
import fr.abes.theses.thesesmisc.tasklets.theseecritacademiquechunk.TefWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.List;

@Component
public class ScinderVedetteRameauCompositeItemWriter extends CompositeItemWriter<DocumentProcess> {

    private final CsvStatusWriter csvStatusWriter;

    @Autowired
    public ScinderVedetteRameauCompositeItemWriter(
            TefWriter tefWriter,
            CsvStatusWriter csvStatusWriter) {
        this.csvStatusWriter = csvStatusWriter;
        setDelegates(List.of(tefWriter, csvStatusWriter));
    }

    @PreDestroy
    public void cleanup() {
        csvStatusWriter.close();
    }
}