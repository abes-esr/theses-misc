package fr.abes.theses.thesesmisc.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "COMPTE")
@NoArgsConstructor
@Getter
@Setter
public class CompteSTEP {
    @Id
    @Column(name = "IDCOMPTE")
    private Integer idCompte;

    @Column(name = "LOGIN")
    private String login;

    @Column(name = "MDP")
    private String mdp;

    @Column(name = "IDDOC")
    private Integer idDoc;

    @Column(name = "CODEETAB")
    private String CODEETAB;

    @Column(name = "DTCREA")
    private String DTCREA;

    @Column(name = "DTMODIF")
    private String DTMODIF;

    @Column(name = "ESTACTIF")
    private String ESTACTIF;

    @Column(name = "PSEUDO")
    private String PSEUDO;

    @Column(name = "ROLE", insertable = false, updatable = false)
    private String role;

/*    @Column(name = "PSEUDO")
    private String pseudo;*/

/*    @Override
    public Integer getId() {
        return idCompte;
    }*/
}

