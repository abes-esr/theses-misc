package fr.abes.theses.thesesmisc.tasklets.csv;

import fr.abes.theses.thesesmisc.model.DocumentProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Component
public class CsvStatusWriter implements ItemStreamWriter<DocumentProcess> {
    private final String csvFilePath;
    private PrintWriter writer;

    public CsvStatusWriter(@Value("${csv.scinder.vedette.rameau.status.file.path}") String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        try {
            Path path = Paths.get(csvFilePath);
            Path parent = path.getParent();

            if (parent != null) {
                Files.createDirectories(parent);
            }

            BufferedWriter bufferedWriter = Files.newBufferedWriter(path);
            writer = new PrintWriter(bufferedWriter);

            writer.println("id\tstatus");
            writer.flush();
        } catch (IOException e) {
            throw new ItemStreamException("Unable to open CSV status file: " + csvFilePath, e);
        }
    }

    @Override
    public void write(List<? extends DocumentProcess> items) {
        if (writer == null) {
            throw new IllegalStateException("CsvStatusWriter has not been opened before write(). Check Spring Batch writer/stream configuration.");
        }

        for (DocumentProcess item : items) {
            if (item == null || item.document == null) {
                log.warn("Skipping null DocumentProcess or DocumentProcess with null document");
                continue;
            }

            String line = String.format(
                    "%s\t%s",
                    item.document.getIdDoc(),
                    item.edited ? "TRAITE" : "NON_TRAITE"
            );

            writer.println(line);
        }

        writer.flush();
    }

    @Override
    public void update(ExecutionContext executionContext) {
        // Nothing to persist between chunks.
    }

    @Override
    public void close() {
        if (writer != null) {
            writer.close();
            writer = null;
        }
    }
}