package fr.abes.theses.thesesmisc.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ScissionRameauList {
    private static final List<ScissionRameauEntry> scissionRameauList = new ArrayList<>();
    private static final List<Integer> scissionRameauTefIdList = new ArrayList<>();


    public static void loadScissionRameauList() throws IOException {
        Reader in;
        try {
            in = new FileReader("src/main/resources/autorites_rameau_td_scission_liste.csv");
        } catch (Exception e) {
            in = new FileReader("autorites_rameau_td_scission_liste.csv");
        }

        final Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withDelimiter('\t')
                .withFirstRecordAsHeader()
                .parse(in);

        for (final CSVRecord record : records) {
            try {
                // Récupération des valeurs
                String oldPpn = record.get("PPN A REMPLACER");
                String oldLabel = record.get("LIBELLE PPN A REMPLACER");
                String newPpn1 = record.get("PPN 1 DE REMPLACEMENT");
                String newLabel1 = record.get("LIBELLE PPN 1 DE  REMPLACEMENT");
                String newPpn2 = record.get("PPN 2 DE REMPLACEMENT");
                String newLabel2 = record.get("LIBELLE PPN 2 DE  REMPLACEMENT");

                // Vérification des champs obligatoires
                if (oldPpn == null || oldPpn.trim().isEmpty() ||
                        oldLabel == null || oldLabel.trim().isEmpty() ||
                        newPpn1 == null || newPpn1.trim().isEmpty() ||
                        newLabel1 == null || newLabel1.trim().isEmpty() ||
                        newPpn2 == null || newPpn2.trim().isEmpty() ||
                        newLabel2 == null || newLabel2.trim().isEmpty()) {
                    log.error("Ligne ignorée (champ manquant) : " + record.toString());
                    continue;
                }

                // Vérification de la longueur des PPN (9 caractères)
                if (oldPpn.length() != 9 || newPpn1.length() != 9 || newPpn2.length() != 9) {
                    log.error("Ligne ignorée (PPN invalide) : PPN doit faire 9 caractères. Ligne : " + record.toString());
                    continue;
                }

                // Si tout est valide, on ajoute à la liste
                scissionRameauList.add(new ScissionRameauEntry(oldPpn, newPpn1, newLabel1, newPpn2, newLabel2));

            } catch (Exception e) {
                log.error("Erreur lors du traitement de la ligne : " + e.getMessage());
            }
        }
    }

    public static void loadScissionRameauTefIdList() throws IOException {
        Reader in;
        try {
            in = new FileReader("src/main/resources/autorites_rameau_td_scission_liste_tef_id.csv");
        } catch (Exception e) {
            in = new FileReader("autorites_rameau_td_scission_liste_tef_id.csv");
        }
        final Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter('\t').withFirstRecordAsHeader().parse(in);
        for (final CSVRecord record : records) {
            scissionRameauTefIdList.add(Integer.valueOf(record.get("TEF ID")));
        }
    }

    public static List<ScissionRameauEntry> getEntries() {
        return scissionRameauList;
    }

    public static List<Integer> getTefIds() {
        return scissionRameauTefIdList;
    }
}