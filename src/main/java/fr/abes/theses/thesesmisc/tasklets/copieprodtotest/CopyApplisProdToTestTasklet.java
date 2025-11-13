package fr.abes.theses.thesesmisc.tasklets.copieprodtotest;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

@Component
public class CopyApplisProdToTestTasklet implements Tasklet {

    @Value("${codeEtab}")
    private String codeEtab;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        if (codeEtab == null || codeEtab.length() != 4) {
            throw new IllegalArgumentException("Code Etab non valide : " + codeEtab);
        }

        String localPath = "/applis/theses/STARSTOCK/" + codeEtab;
        String remotePath = "pivoine-prod.v106.abes.fr:/applis/portail/theses/STARSTOCK/" + codeEtab;

        runCommand("rm", "-rf", localPath);
        runCommand("rsync", "-av", remotePath + "/", localPath + "/");

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
