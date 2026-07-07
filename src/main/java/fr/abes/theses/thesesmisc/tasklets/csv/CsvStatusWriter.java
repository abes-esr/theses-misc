package fr.abes.theses.thesesmisc.tasklets.csv;

import fr.abes.theses.thesesmisc.model.DocumentProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

@Slf4j
@Component
public class CsvStatusWriter implements ItemWriter<DocumentProcess> {
    private final String csvFilePath;
    private PrintWriter writer;
    private boolean isFirstWrite = true; // Flag pour détecter le premier appel

    public CsvStatusWriter(@Value("${csv.scinder.vedette.rameau.status.file.path}") String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    @Override
    public void write(List<? extends DocumentProcess> items) throws Exception {
        if (isFirstWrite) {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(csvFilePath, false)));
            writer.println("id\tstatus");
            isFirstWrite = false;
        }
        else if (writer == null) {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(csvFilePath, true)));
        }

        for (DocumentProcess item : items) {
            String line = String.format("%s\t%s", item.document.getIdDoc(), item.edited ? "TRAITE" : "NON_TRAITE");
            writer.println(line);
        }
        writer.flush();
    }

    public void close() {
        if (writer != null) {
            writer.close();
        }
    }

    // Réinitialiser le flag pour un nouveau job
    public void reset() {
        this.isFirstWrite = true;
        this.close();
    }
}