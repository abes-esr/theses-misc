package fr.abes.theses.thesesmisc.tasklets.copieprodtotest;

import fr.abes.theses.thesesmisc.service.impl.DocumentService;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CopyBddProdToTestTasklet implements Tasklet {
    private final DocumentService service;
    @Value("${codeEtab}")
    private String codeEtab;

    @Value("${spring.datasource.username}")
    private String username;

    public CopyBddProdToTestTasklet(DocumentService service) {
        this.service = service;
    }


    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        switch (username) {
            case "STAR" :
                service.getDao().getDocument().copyProdToTestStar(codeEtab);
                return RepeatStatus.FINISHED;
            case "SUJETS":
                service.getDao().getDocument().copyProdToTestSujets(codeEtab);
                return RepeatStatus.FINISHED;
            default:
                throw new IllegalArgumentException("Username non valide : " + username);
        }
    }
}
