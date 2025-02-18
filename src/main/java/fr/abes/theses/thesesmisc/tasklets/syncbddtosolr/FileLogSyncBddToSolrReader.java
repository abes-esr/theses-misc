package fr.abes.theses.thesesmisc.tasklets.syncbddtosolr;

import fr.abes.theses.thesesmisc.model.DocumentProcess;
import fr.abes.theses.thesesmisc.model.IdToChange;
import fr.abes.theses.thesesmisc.service.impl.DocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class FileLogSyncBddToSolrReader implements ItemReader<DocumentProcess> {

    private final DocumentService service;
    private List<String> iddocList = new ArrayList<>();
    private AtomicInteger iIds = new AtomicInteger();
    public FileLogSyncBddToSolrReader(DocumentService service) throws IOException {

        this.service = service;

        final String string = Files.readString(Path.of("src/main/resources/indexationsolr-C2.log"), StandardCharsets.UTF_8);

        final String regex = "contexte = star.{30,150}iddoc = (\\d+).{300,350}http:\\/\\/denim\\.v102\\.abes\\.fr:8080\\/solrSuj";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL);
        final Matcher matcher = pattern.matcher(string);

        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                iddocList.add(matcher.group(i));
            }
        }
    }

    @Override
    public DocumentProcess read() throws Exception {
        if (this.iIds.get() < this.iddocList.size()) {
            String id = this.iddocList.get(this.iIds.getAndIncrement());
            IdToChange idToC = new IdToChange();
            idToC.id = id;
            return new DocumentProcess(
                    this.service.getDao().getDocument().findById(Integer.valueOf(id)).orElse(null),
                    idToC);
        }
        return null;
    }
}
