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
        final Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter('\t').withFirstRecordAsHeader().parse(in);
        for (final CSVRecord record : records) {
            try {
            String oldPpn = getRequiredValue(record, "PPN A REMPLACER");
            String newPpn1 = getRequiredValue(record, "PPN 1 DE REMPLACEMENT");
            String newLabel1 = getRequiredValue(record, "LIBELLE PPN 1 DE  REMPLACEMENT");
            String newPpn2 = getRequiredValue(record, "PPN 2 DE REMPLACEMENT");
            String newLabel2 = getRequiredValue(record, "LIBELLE PPN 2 DE  REMPLACEMENT");

            scissionRameauList.add(new ScissionRameauEntry(oldPpn, newPpn1, newLabel1, newPpn2, newLabel2));
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    private static String getRequiredValue(CSVRecord record, String columnName) {
        String value = record.get(columnName);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Colonne vide dans le fichier de scission RAMEAU à la ligne "
                            + record.getRecordNumber()
                            + " : "
                            + columnName
            );
        }
        return value;
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