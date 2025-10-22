package fr.abes.theses.thesesmisc.tasklets.theseecritacademiquechunk;

import fr.abes.theses.thesesmisc.model.DocumentProcess;
import fr.abes.theses.thesesmisc.service.impl.DocumentService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
public class TefWriter implements ItemWriter<DocumentProcess> {

    @Getter
    private final DocumentService service;

    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    public TefWriter(DocumentService service) {
        this.service = service;
    }

    @Override
    public void write(List<? extends DocumentProcess> list) {
        for (DocumentProcess documentProcess : list) {
            if (documentProcess.edited) {
                try {
                    service.getDao().getDocument().save(documentProcess.document);
                    log.info("idDoc edited : " + documentProcess.document.getIdDoc());
                } catch (Exception e) {
                    log.error("Error in writer, doc : " + documentProcess.document.getIdDoc());
                }
            } else {
                if (documentProcess.document != null) {
                    log.info("Doc not edited IdDoc : " + documentProcess.document.getIdDoc());
                } else {
                    log.warn("Le document est null, vérifier l'environnement STAR/SUJETS");
                }
            }

            try (Connection conn = DriverManager.getConnection(datasourceUrl, username, password)) {

                // Préparation de l'appel de la procédure
                try (CallableStatement cs = conn.prepareCall("{ call PORTAIL.AJOUTER_DOCUMENT_INDEXATION_SOLR(?, ?, ?) }")) {

                    // Définition des paramètres IN
                    cs.setInt(1, documentProcess.document.getIdDoc());        // p_iddoc
                    cs.setString(2, "add");     // p_action
                    cs.setString(3, "star");    // p_origin

                    // Exécution
                    cs.execute();

                    System.out.println("Procédure exécutée avec succès !");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

 /*               if (documentProcess.compte != null) {
                    try {
                        service.getDao().getCompte().save(documentProcess.compte);
                        log.info("Compte edited : " + documentProcess.compte.getIdCompte());
                    } catch (Exception e) {
                        log.error("Error in writer, Compte IdDoc : " + documentProcess.document.getIdDoc());
                    }
                } else {
                    log.error("Compte not edited IdDoc : " + documentProcess.document.getIdDoc());
                }*/
        }
    }
}
