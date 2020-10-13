package fr.abes.theses.deletesubdivision.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "COMPTE")
@NoArgsConstructor
@Getter
@Setter
public class Compte implements Serializable, GenericEntity<Integer>  {

    @Id
    @Column(name = "IDCOMPTE")
    private Integer idCompte;

    @Column(name = "LOGIN")
    private String login;

    @Column(name = "MDP")
    private String mdp;

    @Column(name = "IDDOC")
    private Integer idDoc;

    @Column(name = "MELINSTI")
    private String MELINSTI;

    @Column(name = "MELPERSO")
    private String MELPERSO;

    @Column(name = "ADRESSE")
    private String ADRESSE;

    @Column(name = "CODEPOSTAL")
    private String CODEPOSTAL;

    @Column(name = "VILLE")
    private String VILLE;

    @Column(name = "PAYS")
    private String PAYS;

    @Column(name = "SEXE")
    private String SEXE;

    @Column(name = "ROLE")
    private String ROLE;

    @Column(name = "DISPLAYNAME")
    private String DISPLAYNAME;

    @Column(name = "NUMIDENT")
    private String NUMIDENT;

    @Column(name = "CODEETAB")
    private String codeEtab;

    @Column(name = "NOM")
    private String NOM;

    @Column(name = "PRENOM")
    private String PRENOM;

    @Column(name = "TEL")
    private String TEL;

    @Column(name = "DTCREA")
    private Date dtCrea;

    @Column(name = "DTMODIF")
    private Date dtModif;

    @Column(name = "ESTACTIF")
    private Integer estActif;

    @Column(name = "ROLE", insertable = false, updatable = false)
    private String role;

/*    @Column(name = "PSEUDO")
    private String pseudo;*/

    @Override
    public Integer getId() {
        return idCompte;
    }
}
