package fr.abes.theses.thesesmisc.tasklets.copieprodtotest;

import fr.abes.theses.thesesmisc.model.DocumentProcess;
import fr.abes.theses.thesesmisc.model.Tef;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CopyProdToTestProcessor implements ItemProcessor<DocumentProcess, DocumentProcess> {

    @Value("${copyProdToTest.fileAppli}")
    private boolean copyFileAppli;

    @Value("${spring.datasource.username}")
    private String username;

    String starStock = "/applis/theses/STARSTOCK/";

    @Override
    public DocumentProcess process(DocumentProcess documentProcess) throws Exception {

        Tef documentTef = new Tef(documentProcess.document.getDoc());
        String oldId = documentTef.getIdInTEF();

        documentProcess.edited = documentTef.searchAndReplace(
                String.valueOf(documentProcess.document.getIdDoc()),
                oldId,
                String.valueOf(documentProcess.document.getIdDoc()));

        documentProcess.document.setDoc(documentTef.documentTef.asXML());

        if (copyFileAppli && "STAR".equals(username)) {
            renameFolderInApplis(oldId, String.valueOf(documentProcess.document.getIdDoc()), documentProcess.document.getCodeEtab());
            ajoutePdfFactice(String.valueOf(documentProcess.document.getIdDoc()), documentProcess.document.getCodeEtab());
        }

        return documentProcess;
    }

    private void ajoutePdfFactice(String iddoc, String codeEtab) {
        String path = starStock + codeEtab + "/THESE_" + iddoc;

        if (path.contains("portail")) {
            throw new IllegalArgumentException("LocalPath non valide : " + path);
        }

        Path root = Path.of(path);

        try {
            List<Path> targetFolders = findDocumentLevel2Folders(root);

            if (targetFolders.isEmpty()) {
                log.warn("Aucun dossier document/*/*/ trouvé pour " + path);
                return;
            }

            for (Path folder : targetFolders) {
                Path pdfDest = folder.resolve("PDF_" + iddoc + ".pdf");
                copyResource("/TESTarchivageCines.pdf", pdfDest);
            }

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'ajout PDF factice pour " + path, e);
        }
    }

    private boolean renameFolderInApplis(String oldId, String newId, String codeEtab) {
        String oldPath = starStock + codeEtab + "/THESE_" + oldId;
        String newPath = starStock + codeEtab + "/THESE_" + newId;

        File oldFile = new File(oldPath);
        File newFile = new File(newPath);

        if (!oldFile.exists()) {
            log.error("Le fichier " + oldFile.getAbsolutePath() + " n'existe pas.");
            return false;
        }

        return oldFile.renameTo(newFile);
    }

    private void copyResource(String resourcePath, Path destination) throws IOException {
        try (InputStream in = getClass().getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new FileNotFoundException("Ressource non trouvée : " + resourcePath);
            }
            //Files.createDirectories(destination.getParent());
            Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private List<Path> findDocumentLevel2Folders(Path localPath) throws IOException {
        Path documentRoot = localPath.resolve("document");

        if (!Files.exists(documentRoot)) {
            return List.of();
        }

        try (var stream = Files.walk(documentRoot, 2)) {
            return stream
                    .filter(Files::isDirectory)
                    .filter(path -> path.getNameCount() == documentRoot.getNameCount() + 2)
                    .collect(Collectors.toList());
        }
    }
}
