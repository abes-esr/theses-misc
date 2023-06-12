package fr.abes.theses.thesesmisc.tasklets.checkIndexBdd;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CheckIndexBddWriter implements ItemWriter<String> {
    @Override
    public void write(List<? extends String> list) throws Exception {

    }
}
