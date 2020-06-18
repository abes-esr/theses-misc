package fr.abes.theses.deletesubdivision.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "DOCUMENT")
@NoArgsConstructor
@Getter
@Setter
public class Document implements Serializable, GenericEntity<Integer> {

    @Id
    @Column(name = "IDDOC")
    private Integer idDoc;

    @ColumnTransformer(read = "NVL2(DOC, (DOC).getClobVal(), NULL)", write = "NULLSAFE_XMLTYPE(?)")
    @Lob
    @Column(name = "DOC", columnDefinition = "XMLType")
    private String doc;

/*    @Column(name = "TEXTE")
    private String texte;*/

    @Column(name = "CODEETAB")
    private String codeEtab;

/*    @Column(name = "ENVOISOLR")
    private Integer envoiSolr;*/

    public Document(Integer idDoc, String doc, String texte, String codeEtab, Integer envoiSolr) {
        this.idDoc = idDoc;
        this.doc = doc;
        //this.texte = texte;
        this.codeEtab = codeEtab;
        //this.envoiSolr = envoiSolr;
    }


    @Override
    public Integer getId() {
        return idDoc;
    }




}
