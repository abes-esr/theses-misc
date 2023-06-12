package fr.abes.theses.thesesmisc.tasklets.checkIndexBdd;

import fr.abes.theses.thesesmisc.model.BddData;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class CheckIndexBddProcessor implements ItemProcessor<BddData, String> {
    @Override
    public String process(BddData bddData) throws Exception {
        return null;
    }
}
