package fr.abes.theses.thesesmisc.tasklets.copieprodtotest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CopyApplisProdToTestTasklet implements Tasklet {

    @Value("${codeEtab}")
    private String codeEtab;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        if (codeEtab == null || codeEtab.length() != 4) {
            throw new IllegalArgumentException("Code Etab non valide : " + codeEtab);
        }

        /*
        2.5G    THESE_158692
3.1G    UNIP/THESE_170401
3.3G    UNIP/THESE_188332
3.4G    UNIP/THESE_122971
3.8G    UNIP/THESE_164135
        */

        String localPath = "/applis/theses/STARSTOCK/" + codeEtab;
        String remotePath = "pivoine-prod.v106.abes.fr:/applis/portail/theses/STARSTOCK/" + codeEtab;

        if (localPath.contains("portail")) {
            throw new IllegalArgumentException("LocalPath non valide : " + localPath);
        }

        log.info("rm de " + localPath );
        runCommand("rm", "-rf", localPath);

        log.info("rsync de " + remotePath + " vers " + localPath );
        runCommand("rsync", "-a",
                "--max-size=1m",
                "--exclude=*.pdf",
                "--exclude=*.zip",
                remotePath + "/", localPath + "/");

        return RepeatStatus.FINISHED;
    }

    private String runCommand(String... command) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.inheritIO(); // redirige stdout/stderr vers la console du job
        Process process = pb.start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Commande échouée : " + String.join(" ", command) + " (exit=" + exitCode + ")");
        }
        return pb.toString();
    }
}
