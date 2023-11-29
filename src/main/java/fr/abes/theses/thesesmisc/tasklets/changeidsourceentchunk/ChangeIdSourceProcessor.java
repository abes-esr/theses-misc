package fr.abes.theses.thesesmisc.tasklets.changeidsourceentchunk;

import fr.abes.theses.thesesmisc.model.DocumentProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChangeIdSourceProcessor implements ItemProcessor<DocumentProcess, DocumentProcess> {
    @Override
    public DocumentProcess process(DocumentProcess documentProcess) throws Exception {
        String oldIdSource = "0350936C";
        String newIdSource = "0353074B";
        // Change IdSourceEnt
        try {
            if (!documentProcess.compte.getLogin().contains(newIdSource)){
                log.info("Id STAR : " + documentProcess.idToChange.id);
            }
            //Tef documentTef = new Tef(documentProcess.document.getDoc());
            //documentProcess.edited = documentTef.changeIdSourceStar(oldIdSource, newIdSource);
            //documentProcess.edited = documentTef.changeIdSourceStep(oldIdSource, newIdSource);
            //documentProcess.document.setDoc(documentTef.documentTef.asXML());
        } catch (Exception e){
            log.error("Error in processor, doc : " + documentProcess.idToChange.id + "\n" + e);
        }

        // Change LOGIN,MDP et le NUMIDENT

        try {
            //STAR
            /*documentProcess.compte.setLogin(documentProcess.compte.getLogin().replace(oldIdSource, newIdSource));
            documentProcess.compte.setMdp(documentProcess.compte.getMdp().replace(oldIdSource, newIdSource));
            documentProcess.compte.setNUMIDENT(documentProcess.compte.getNUMIDENT().replace(oldIdSource, newIdSource));*/
            //STEP
            //documentProcess.compteSTEP.setLogin(documentProcess.compteSTEP.getLogin().replace(oldIdSource, newIdSource));
        } catch (Exception e) {
            log.error("Error in processor, compte : " + documentProcess.idToChange.id + "\n" + e);
        }


        return documentProcess;
    }
}
